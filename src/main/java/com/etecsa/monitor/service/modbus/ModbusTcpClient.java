package com.etecsa.monitor.service.modbus;

import com.etecsa.monitor.domain.MapeoRegistro;
import com.etecsa.monitor.domain.enumeration.TipoDato;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Driver Modbus TCP ligero — sin dependencias externas.
 *
 * Implementa Function Code 0x03 (Read Holding Registers) sobre TCP.
 * Cada instancia maneja UNA conexión a un gateway (ip:puerto).
 * Los esclavos (slaves) se consultan cambiando el unitId en el ADU.
 *
 * Protocolo Modbus TCP:
 *   [TxId 2B][ProtoId 2B][Length 2B][UnitId 1B][FC 1B][StartAddr 2B][Qty 2B]
 *   Response:
 *   [TxId 2B][ProtoId 2B][Length 2B][UnitId 1B][FC 1B][ByteCount 1B][Data nB]
 */
@Component
public class ModbusTcpClient {

    private static final Logger LOG = LoggerFactory.getLogger(ModbusTcpClient.class);

    private static final int MODBUS_PORT_DEFAULT = 502;
    private static final int PROTOCOL_ID = 0;
    private static final int FC_READ_HOLDING = 0x03;
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 3000;
    private static final int MAX_REGISTERS_PER_REQUEST = 100;

    /** Byte order para Modbus: Big-Endian (MSB first) */
    private static final ByteOrder MODBUS_BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    /**
     * Lee registros Modbus de un gateway y devuelve Map<campoSql, valorNumerico>.
     * Aplica escala según el MapeoRegistro de cada campo.
     *
     * @param host      IP del gateway
     * @param port      Puerto del gateway (tipicamente 502)
     * @param slaveId   Unit ID del inversor (1-247)
     * @param mapeos    Lista de registros a leer (direccionRegistro, tipoDato, escala, campoSql)
     * @return          Map campoSql → valor (Double), o Map vacío si hay error
     */
    public Map<String, Double> readRegisters(String host, int port, int slaveId, List<MapeoRegistro> mapeos) {
        if (mapeos == null || mapeos.isEmpty()) {
            return Collections.emptyMap();
        }

        // Agrupar mapeos en bloques contiguos para minimizar pedidos Modbus
        List<RegisterBlock> blocks = groupIntoBlocks(mapeos);

        Map<String, Double> result = new LinkedHashMap<>();

        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            int transactionId = 1;

            for (RegisterBlock block : blocks) {
                byte[] request = buildReadRequest(transactionId, slaveId, block.startAddr, block.quantity);
                out.write(request);
                out.flush();

                byte[] response = readResponse(in, transactionId);
                if (response != null) {
                    parseResponse(response, block, result);
                }
                transactionId++;
            }
        } catch (IOException e) {
            LOG.warn("Error Modbus TCP {}/{} slaveId={}: {}", host, port, slaveId, e.getMessage());
        }

        return result;
    }

    /**
     * Lee registros con latencia measure. Devuelve resultado + latencia en ms.
     */
    public ModbusReadResult readRegistersWithLatency(String host, int port, int slaveId, List<MapeoRegistro> mapeos) {
        long start = System.currentTimeMillis();
        Map<String, Double> data = readRegisters(host, port, slaveId, mapeos);
        int latencyMs = (int) (System.currentTimeMillis() - start);
        return new ModbusReadResult(data, latencyMs);
    }

    // ========== Protocolo Modbus TCP ==========

    /**
     * Construye pedido FC03 Read Holding Registers.
     * ADU: [TxId 2B][ProtoId 2B][Length 2B=6][UnitId 1B][FC 1B][StartAddr 2B][Qty 2B]
     */
    private byte[] buildReadRequest(int txId, int slaveId, int startAddr, int quantity) {
        ByteBuffer buf = ByteBuffer.allocate(12);
        buf.order(MODBUS_BYTE_ORDER);
        buf.putShort((short) txId); // Transaction ID
        buf.putShort((short) PROTOCOL_ID); // Protocol ID (0 = Modbus)
        buf.putShort((short) 6); // Length (bytes que siguen: unit+fc+addr+qty)
        buf.put((byte) slaveId); // Unit ID (slave)
        buf.put((byte) FC_READ_HOLDING); // Function Code
        buf.putShort((short) startAddr); // Start Address
        buf.putShort((short) quantity); // Quantity of registers
        return buf.array();
    }

    /**
     * Lee y valida respuesta Modbus TCP.
     * Devuelve solo el payload (los bytes de datos de registros), sin el header MBAP.
     */
    private byte[] readResponse(InputStream in, int expectedTxId) throws IOException {
        // Header MBAP: 7 bytes
        byte[] header = readExact(in, 7);
        ByteBuffer headerBuf = ByteBuffer.wrap(header).order(MODBUS_BYTE_ORDER);

        int txId = headerBuf.getShort() & 0xFFFF;
        int protoId = headerBuf.getShort() & 0xFFFF;
        int length = headerBuf.getShort() & 0xFFFF;
        int unitId = headerBuf.get() & 0xFF;

        if (txId != expectedTxId) {
            LOG.warn("Modbus TxId mismatch: expected={}, got={}", expectedTxId, txId);
            return null;
        }

        // Leer el resto del PDU: length-1 bytes (length ya incluye el unitId)
        int pduLength = length - 1;
        byte[] pdu = readExact(in, pduLength);

        int fc = pdu[0] & 0xFF;

        // Error response: FC con bit 7 set + exception code
        if (fc >= 0x80) {
            int exceptionCode = pdu[1] & 0xFF;
            LOG.warn("Modbus error response: FC=0x{}, exception=0x{}", Integer.toHexString(fc), Integer.toHexString(exceptionCode));
            return null;
        }

        // FC 0x03 response: [FC 1B][ByteCount 1B][Data nB]
        int byteCount = pdu[1] & 0xFF;
        if (pdu.length < 2 + byteCount) {
            LOG.warn("Modbus response too short: expected {} data bytes, got {}", byteCount, pdu.length - 2);
            return null;
        }

        byte[] data = new byte[byteCount];
        System.arraycopy(pdu, 2, data, 0, byteCount);
        return data;
    }

    /**
     * Parsea los datos de respuesta y los mapea a campoSql → valor con escala.
     */
    private void parseResponse(byte[] data, RegisterBlock block, Map<String, Double> result) {
        for (MapeoRegistro mapeo : block.mapeos) {
            int offset = (mapeo.getDireccionRegistro() - block.startAddr) * 2;
            Double rawValue = decodeRegister(data, offset, mapeo.getTipoDato());

            if (rawValue != null) {
                // Aplicar escala
                Double escala = mapeo.getEscala();
                double finalValue = (escala != null && escala != 0.0) ? rawValue / escala : rawValue;
                result.put(mapeo.getCampoSql(), finalValue);
            }
        }
    }

    /**
     * Decodifica un registro Modbus según su tipo de dato.
     * Los registros son de 2 bytes (16-bit). Para tipos 32-bit se leen 2 registros contiguos.
     */
    private Double decodeRegister(byte[] data, int offset, TipoDato tipoDato) {
        if (offset < 0 || offset >= data.length) {
            return null;
        }

        try {
            switch (tipoDato) {
                case UnsignedInt16:
                    return (double) (((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF));
                case SignedInt16:
                    short signed = (short) (((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF));
                    return (double) signed;
                case UnsignedInt32:
                    if (offset + 3 >= data.length) return null;
                    long unsigned32 =
                        ((long) (data[offset] & 0xFF) << 24) |
                        ((long) (data[offset + 1] & 0xFF) << 16) |
                        ((long) (data[offset + 2] & 0xFF) << 8) |
                        ((long) (data[offset + 3] & 0xFF));
                    return (double) unsigned32;
                case SignedInt32:
                    if (offset + 3 >= data.length) return null;
                    int signed32 =
                        ((data[offset] & 0xFF) << 24) |
                        ((data[offset + 1] & 0xFF) << 16) |
                        ((data[offset + 2] & 0xFF) << 8) |
                        (data[offset + 3] & 0xFF);
                    return (double) signed32;
                case FloatIEEE754:
                    if (offset + 3 >= data.length) return null;
                    int floatBits =
                        ((data[offset] & 0xFF) << 24) |
                        ((data[offset + 1] & 0xFF) << 16) |
                        ((data[offset + 2] & 0xFF) << 8) |
                        (data[offset + 3] & 0xFF);
                    return (double) Float.intBitsToFloat(floatBits);
                case AsciiText:
                    // Para ASCII, devolvemos 0.0 — no es numérico
                    return 0.0;
                default:
                    return null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LOG.warn("Error decoding register at offset {}: {}", offset, e.getMessage());
            return null;
        }
    }

    // ========== Agrupación de registros en bloques contiguos ==========

    /**
     * Agrupa MapeoRegistros en bloques contiguos para minimizar pedidos Modbus.
     * Registros consecutivos se leen en un solo pedido FC03.
     */
    private List<RegisterBlock> groupIntoBlocks(List<MapeoRegistro> mapeos) {
        // Ordenar por dirección
        List<MapeoRegistro> sorted = new ArrayList<>(mapeos);
        sorted.sort(Comparator.comparingInt(MapeoRegistro::getDireccionRegistro));

        List<RegisterBlock> blocks = new ArrayList<>();
        RegisterBlock current = null;

        for (MapeoRegistro mapeo : sorted) {
            int addr = mapeo.getDireccionRegistro();
            int sizeInRegisters = getRegisterSize(mapeo.getTipoDato());

            if (current == null) {
                current = new RegisterBlock(addr);
            } else if (
                addr > current.endAddr && addr <= current.endAddr + 4 && current.quantity + sizeInRegisters <= MAX_REGISTERS_PER_REQUEST
            ) {
                // Gap pequeño y cabe en el bloque → extendemos
                current.quantity = (addr + sizeInRegisters) - current.startAddr;
            } else {
                // Gap grande o bloque lleno → nuevo bloque
                blocks.add(current);
                current = new RegisterBlock(addr);
            }

            current.mapeos.add(mapeo);
            current.endAddr = addr + sizeInRegisters;
            current.quantity = current.endAddr - current.startAddr;
        }

        if (current != null) {
            blocks.add(current);
        }

        return blocks;
    }

    /** Cuántos registros (16-bit) ocupa cada TipoDato */
    private int getRegisterSize(TipoDato tipoDato) {
        switch (tipoDato) {
            case UnsignedInt16:
            case SignedInt16:
                return 1;
            case UnsignedInt32:
            case SignedInt32:
            case FloatIEEE754:
                return 2;
            case AsciiText:
                return 1; // mínima unidad
            default:
                return 1;
        }
    }

    /** Lee exactamente N bytes del InputStream */
    private byte[] readExact(InputStream in, int count) throws IOException {
        byte[] buffer = new byte[count];
        int totalRead = 0;
        while (totalRead < count) {
            int read = in.read(buffer, totalRead, count - totalRead);
            if (read == -1) {
                throw new IOException("End of stream after " + totalRead + " bytes, expected " + count);
            }
            totalRead += read;
        }
        return buffer;
    }

    // ========== Clases internas ==========

    /** Bloque contiguo de registros para un solo pedido Modbus */
    private static class RegisterBlock {

        int startAddr;
        int endAddr;
        int quantity;
        List<MapeoRegistro> mapeos = new ArrayList<>();

        RegisterBlock(int startAddr) {
            this.startAddr = startAddr;
            this.endAddr = startAddr;
            this.quantity = 1;
        }
    }

    /** Resultado de lectura con latencia */
    public static class ModbusReadResult {

        private final Map<String, Double> data;
        private final int latencyMs;

        public ModbusReadResult(Map<String, Double> data, int latencyMs) {
            this.data = data;
            this.latencyMs = latencyMs;
        }

        public Map<String, Double> getData() {
            return data;
        }

        public int getLatencyMs() {
            return latencyMs;
        }
    }
}

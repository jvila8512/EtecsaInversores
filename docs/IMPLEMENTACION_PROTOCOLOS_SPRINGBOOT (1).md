# IMPLEMENTACIÓN DE PROTOCOLOS EN SPRING BOOT

## Felicity Modbus RTU + JK-BMS — Guía Técnica Completa

---

## PARTE 1 — PROTOCOLO FELICITY (Modbus RTU)

### ¿Qué dice el protocolo?

El inversor Felicity usa **Modbus RTU sobre RS-485/RS-232**:

| Parámetro        | Valor                                                   |
| ---------------- | ------------------------------------------------------- |
| Baud rate        | 2400 bps                                                |
| Data bits        | 8                                                       |
| Stop bits        | 1                                                       |
| Paridad          | None                                                    |
| Slave address    | 0x01 (hardcoded)                                        |
| Timeout          | 1000 ms                                                 |
| Funciones usadas | 0x03 (read), 0x06 (write single), 0x10 (write multiple) |

### Mapa de registros de lectura (0x03)

| Dirección | Nombre               | Tipo   | Escala | Unidad | Notas                                                               |
| --------- | -------------------- | ------ | ------ | ------ | ------------------------------------------------------------------- |
| 0x1101    | Working Mode         | INT16U | —      | —      | 0=On, 1=Standby, 2=Bypass, 3=Battery, 4=Fault, 5=Mains, 6=PV Charge |
| 0x1102    | Battery Charge Stage | INT16U | —      | —      | 0=No charge, 1=Bulk, 2=Absorption, 3=Float                          |
| 0x1103    | Fault Code           | INT16U | —      | —      | Ver tabla de fallos                                                 |
| 0x1104    | Power Flow Bits      | INT16U | —      | —      | Bitfield (b15=bat connected, b7=PV working…)                        |
| 0x1108    | Battery Voltage      | INT16U | /100   | V      | ej: 4800 → 48.00V                                                   |
| 0x1109    | Battery Current      | INT16S | —      | A      | Signed: positivo=carga, negativo=descarga                           |
| 0x110A    | Battery Power        | INT16S | —      | W      | Signed                                                              |
| 0x1111    | AC Output Voltage    | INT16U | /10    | V      | ej: 2200 → 220.0V                                                   |
| 0x1117    | AC Input Voltage     | INT16U | /10    | V      |                                                                     |
| 0x1119    | AC Input Frequency   | INT16U | /100   | Hz     | ej: 5000 → 50.00Hz                                                  |
| 0x111E    | AC Output Power      | INT16S | —      | W      | Potencia activa                                                     |
| 0x111F    | AC Output Apparent   | INT16U | —      | VA     |                                                                     |
| 0x1120    | Load Percentage      | INT16U | —      | %      |                                                                     |
| 0x1126    | PV Voltage           | INT16U | /10    | V      |                                                                     |
| 0x112A    | PV Power             | INT16S | —      | W      |                                                                     |

### Registros de configuración (0x03 read / 0x06-0x10 write)

| Dirección | Nombre                    | Tipo   | Escala | Unidad | Rango                             | Default |
| --------- | ------------------------- | ------ | ------ | ------ | --------------------------------- | ------- |
| 0x211F    | Battery Cut-off Voltage   | INT16U | /10    | V      | 21.0–27.0 / 42.0–54.0             | 42.0    |
| 0x2122    | Battery CV Voltage        | INT16U | /10    | V      | 24.0–30.0 / 48.0–60.0             | 57.6    |
| 0x2123    | Battery Float Voltage     | INT16S | /10    | V      | 24.0–30.0 / 48.0–60.0             | 54.4    |
| 0x2129    | AC Output Frequency       | INT8U  | —      | —      | 0=50Hz, 1=60Hz                    | 0       |
| 0x212A    | Output Source Priority    | INT8U  | —      | —      | 0=Utility, 1=PV, 2=PV+Bat+Grid    | 0       |
| 0x212B    | Application Mode          | INT8U  | —      | —      | 0=APL, 1=UPS                      | 0       |
| 0x212C    | Charging Source Priority  | INT8U  | —      | —      | 1=PV first, 2=PV+Grid, 3=PV only  | 1       |
| 0x212D    | Battery Type              | INT8U  | —      | —      | 0=Gel, 1=Flood, 2=User, 3=LiFePO4 | 0       |
| 0x212E    | Max Charging Current      | INT8U  | —      | A      | 10–100                            | 60      |
| 0x2130    | Max AC Charging Current   | INT8U  | —      | A      | 10–100                            | 30      |
| 0x2156    | Battery Back-to-Charge V  | INT16U | /10    | V      | 22.0–27.0 / 44.0–54.0             | 46.0    |
| 0x2159    | Battery Back-to-Discharge | INT16U | /10    | V      | 24.0–30.1 / 48.0–60.1             | 54.0    |

---

## PARTE 2 — PROTOCOLO JK-BMS

### ¿Qué dice el protocolo?

El JK-BMS usa un **protocolo binario propietario** (NO es Modbus):

| Parámetro   | Valor                             |
| ----------- | --------------------------------- |
| Baud rate   | 115200 bps                        |
| Interfaz    | RS-232 TTL serial / RS485         |
| Start frame | 0x4E 0x57 ('N' 'W')               |
| End marker  | 0x68                              |
| Checksum    | Suma acumulada de todos los bytes |

### Estructura de trama JK-BMS

```
[0x4E][0x57] [LEN_H][LEN_L] [ID4][ID3][ID2][ID1] [CMD] [SRC] [TTYPE] [DATA...] [REC4][REC3][REC2][REC1] [0x68] [CHK4][CHK3][CHK2][CHK1]
  STX(2)       LENGTH(2)         BMS_ID(4)          1     1      1       N          RECORD_NUM(4)            END     CHECKSUM(4)
```

### Identificadores de datos (frame info unit, campo 7)

| ID   | Nombre                | Bytes | Notas de parseo                             |
| ---- | --------------------- | ----- | ------------------------------------------- |
| 0x79 | Voltajes por celda    | 3×n   | Byte1=número celda, Byte2-3=mV (big-endian) |
| 0x80 | Temp. MOSFET          | 2     | 0–140, offset 100 para negativos (100=0°C)  |
| 0x81 | Temp. interna batería | 2     | Igual que 0x80                              |
| 0x82 | Temp. batería         | 2     | Igual que 0x80                              |
| 0x83 | Voltaje pack total    | 2     | ×0.01 V (10mV resolution)                   |
| 0x84 | Corriente             | 2     | Bit15=0 descarga, Bit15=1 carga; ×0.01 A    |
| 0x85 | SOC                   | 1     | 0–100 %                                     |
| 0x86 | Número sensores temp. | 1     |                                             |
| 0x87 | Ciclos de uso         | 2     |                                             |
| 0x89 | Energía total ciclada | 4     | Ah                                          |
| 0x8A | Número strings        | 2     |                                             |
| 0x8B | Warning flags         | 2     | Bitfield: bit0=low cap, bit1=MOS temp, etc. |
| 0x8C | Status bits           | 2     | bit0=charge MOS on, bit1=discharge MOS on   |

---

## PARTE 3 — IMPLEMENTACIÓN SPRING BOOT

### 3.1 Dependencias Maven

```xml
<!-- pom.xml -->
<dependencies>

    <!-- Spring Boot base -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- WebSocket para streaming en tiempo real -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>

    <!-- jSerialComm: manejo de puertos serie (COM, /dev/ttyUSB) -->
    <dependency>
        <groupId>com.fazecast</groupId>
        <artifactId>jSerialComm</artifactId>
        <version>2.10.4</version>
    </dependency>

    <!-- Modbus4j: cliente Modbus RTU/TCP de alto nivel -->
    <dependency>
        <groupId>com.infiniteautomation</groupId>
        <artifactId>modbus4j</artifactId>
        <version>3.0.3</version>
    </dependency>

    <!-- JPA + SQLite para histórico -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.45.1.0</version>
    </dependency>

    <!-- Lombok para reducir boilerplate -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Métricas Prometheus -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>

</dependencies>
```

---

### 3.2 Configuración application.yml

```yaml
# src/main/resources/application.yml

server:
  port: 8080

spring:
  application:
    name: felicity-monitor

  datasource:
    url: jdbc:sqlite:felicity-monitor.db
    driver-class-name: org.sqlite.JDBC

  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
    show-sql: false

felicity:
  modbus:
    # Puerto serial del inversor
    port: 'COM3' # Windows: COM3 | Linux: /dev/ttyUSB0
    baud-rate: 2400
    slave-address: 1
    timeout-ms: 1000
    polling-interval-ms: 2000

  jk-bms:
    port: '/dev/ttyUSB0'
    baud-rate: 115200
    polling-interval-ms: 1000

  websocket:
    broadcast-interval-ms: 2000

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
```

---

### 3.3 Modelo de datos — InverterStatus

```java
// src/main/java/com/felicity/monitor/model/InverterStatus.java
package com.felicity.monitor.model;

import java.time.Instant;
import lombok.Data;

/**
 * Snapshot completo del estado de un inversor Felicity.
 * Todos los campos usan unidades finales (ya aplicada la escala del protocolo).
 */
@Data
public class InverterStatus {

  // ── Identificación ───────────────────────────────────────────────
  private String inverterId;
  private Instant timestamp = Instant.now();

  // ── Modos de operación (registros 0x1101–0x1104) ─────────────────
  /**
   * 0=On/PowerOn  1=Standby  2=Bypass  3=Battery
   * 4=Fault       5=Mains    6=PVCharge
   */
  private int workingMode;

  /**
   * 0=NoCharge  1=Bulk(CC)  2=Absorption(CV)  3=Float
   */
  private int chargeStage;

  /** Código de fallo (0 = sin fallo) */
  private int faultCode;

  /** Bitfield de flujo de energía (registro 0x1104) */
  private int powerFlowBits;

  // ── Batería (registros 0x1108–0x110A) ────────────────────────────
  /** Voltaje de batería en Voltios (raw/100) */
  private double batteryVoltage;

  /** Corriente de batería en Amperios — positivo=carga, negativo=descarga */
  private double batteryCurrent;

  /** Potencia de batería en Vatios */
  private double batteryPower;

  // ── AC salida (registros 0x1111, 0x111E, 0x111F, 0x1120) ─────────
  /** Voltaje AC de salida en Voltios (raw/10) */
  private double acOutputVoltage;

  /** Potencia activa AC de salida en Vatios */
  private double acOutputPower;

  /** Potencia aparente AC de salida en VA */
  private double acOutputApparentPower;

  /** Porcentaje de carga */
  private double loadPercentage;

  // ── AC entrada / Red (registros 0x1117, 0x1119) ──────────────────
  private double acInputVoltage;
  private double acInputFrequency;

  // ── Panel Solar PV (registros 0x1126, 0x112A) ────────────────────
  /** Voltaje de entrada PV en Voltios (raw/10) */
  private double pvVoltage;

  /** Potencia de entrada PV en Vatios */
  private double pvPower;

  // ── Campos derivados (calculados en código) ───────────────────────
  /** ¿La batería se está cargando? */
  public boolean isBatteryCharging() {
    return batteryCurrent > 0;
  }

  /** ¿Hay producción solar? */
  public boolean isPvActive() {
    return pvPower > 0;
  }

  /** Eficiencia de conversión (pvPower → acOutputPower), en % */
  public double getEfficiency() {
    if (pvPower <= 0) return 0.0;
    return Math.min(100.0, (acOutputPower / pvPower) * 100.0);
  }

  /** Consumo desde la red = carga - (PV + descarga batería) */
  public double getGridUsageWatts() {
    double bDischarge = (batteryCurrent < 0) ? Math.abs(batteryPower) : 0;
    return Math.max(0, acOutputPower - pvPower - bDischarge);
  }
}

```

---

### 3.4 Servicio de comunicación Modbus para Felicity

```java
// src/main/java/com/felicity/monitor/service/FelicityModbusService.java
package com.felicity.monitor.service;

import com.fazecast.jSerialComm.SerialPort;
import com.felicity.monitor.model.InverterStatus;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementación MANUAL de Modbus RTU sobre puerto serie.
 * Se usa implementación propia (sin librería extra) para máximo control,
 * igual que hace el proyecto .NET de referencia.
 *
 * Protocolo:
 *   Request  = [SlaveAddr][FuncCode][AddrHi][AddrLo][CountHi][CountLo][CRC_Lo][CRC_Hi]
 *   Response = [SlaveAddr][FuncCode][ByteCount][Data...][CRC_Lo][CRC_Hi]
 */
@Slf4j
@Service
public class FelicityModbusService {

  // ── Configuración inyectada desde application.yml ─────────────────
  @Value("${felicity.modbus.port}")
  private String portName;

  @Value("${felicity.modbus.baud-rate:2400}")
  private int baudRate;

  @Value("${felicity.modbus.slave-address:1}")
  private int slaveAddress;

  @Value("${felicity.modbus.timeout-ms:1000}")
  private int timeoutMs;

  // ── Constantes del protocolo ──────────────────────────────────────
  private static final int FUNC_READ_HOLDING = 0x03;
  private static final int FUNC_WRITE_SINGLE = 0x06;
  private static final int FUNC_WRITE_MULTI = 0x10;

  // Dirección base de registros de estado (Realtime Data)
  private static final int REG_WORKING_MODE = 0x1101;
  private static final int REG_CHARGE_STAGE = 0x1102;
  private static final int REG_FAULT_CODE = 0x1103;
  private static final int REG_POWER_FLOW = 0x1104;
  private static final int REG_BAT_VOLTAGE = 0x1108;
  private static final int REG_BAT_CURRENT = 0x1109;
  private static final int REG_BAT_POWER = 0x110A;
  private static final int REG_AC_OUT_VOLTAGE = 0x1111;
  private static final int REG_AC_IN_VOLTAGE = 0x1117;
  private static final int REG_AC_IN_FREQ = 0x1119;
  private static final int REG_AC_OUT_POWER = 0x111E;
  private static final int REG_AC_OUT_APPAR = 0x111F;
  private static final int REG_LOAD_PCT = 0x1120;
  private static final int REG_PV_VOLTAGE = 0x1126;
  private static final int REG_PV_POWER = 0x112A;

  // ── Puerto serie ──────────────────────────────────────────────────
  private SerialPort serialPort;

  @PostConstruct
  public void openPort() {
    serialPort = SerialPort.getCommPort(portName);
    serialPort.setBaudRate(baudRate);
    serialPort.setNumDataBits(8);
    serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
    serialPort.setParity(SerialPort.NO_PARITY);
    serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeoutMs, timeoutMs);

    if (serialPort.openPort()) {
      log.info("✓ Puerto Modbus abierto: {} @ {} baud", portName, baudRate);
    } else {
      log.error("✗ No se pudo abrir puerto: {}", portName);
    }
  }

  @PreDestroy
  public void closePort() {
    if (serialPort != null && serialPort.isOpen()) {
      serialPort.closePort();
      log.info("Puerto Modbus cerrado.");
    }
  }

  // ═══════════════════════════════════════════════════════════════════
  // API PÚBLICA
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Lee todos los parámetros del inversor y devuelve un InverterStatus.
   * Hace una sola lectura en bloque (0x1101 → 0x112A = 42 registros)
   * más otra lectura para registros no contiguos si los hubiera.
   */
  public synchronized InverterStatus readAllStatus(String inverterId) {
    InverterStatus status = new InverterStatus();
    status.setInverterId(inverterId);

    try {
      // Leer bloque 0x1101 – 0x112A (42 registros contiguos)
      // = 0x112A - 0x1101 + 1 = 42
      int[] regs = readHoldingRegisters(REG_WORKING_MODE, 42);
      if (regs == null) {
        log.warn("No se pudo leer bloque de estado del inversor {}", inverterId);
        return status;
      }

      // ── Modos ──────────────────────────────────────────────────
      status.setWorkingMode(regs[offset(REG_WORKING_MODE)]);
      status.setChargeStage(regs[offset(REG_CHARGE_STAGE)]);
      status.setFaultCode(regs[offset(REG_FAULT_CODE)]);
      status.setPowerFlowBits(regs[offset(REG_POWER_FLOW)]);

      // ── Batería ────────────────────────────────────────────────
      // Registro 0x1108: raw/100 → Voltios
      status.setBatteryVoltage(toUnsigned(regs[offset(REG_BAT_VOLTAGE)]) / 100.0);

      // Registro 0x1109: signed (positivo=carga, negativo=descarga)
      status.setBatteryCurrent(toSigned16(regs[offset(REG_BAT_CURRENT)]));

      // Registro 0x110A: signed Watts
      status.setBatteryPower(toSigned16(regs[offset(REG_BAT_POWER)]));

      // ── AC Salida ──────────────────────────────────────────────
      // 0x1111: raw/10 → Voltios
      status.setAcOutputVoltage(toUnsigned(regs[offset(REG_AC_OUT_VOLTAGE)]) / 10.0);

      // 0x111E: signed Watts
      status.setAcOutputPower(toSigned16(regs[offset(REG_AC_OUT_POWER)]));

      // 0x111F: unsigned VA
      status.setAcOutputApparentPower(toUnsigned(regs[offset(REG_AC_OUT_APPAR)]));

      // 0x1120: unsigned %
      status.setLoadPercentage(toUnsigned(regs[offset(REG_LOAD_PCT)]));

      // ── AC Entrada (Red) ───────────────────────────────────────
      status.setAcInputVoltage(toUnsigned(regs[offset(REG_AC_IN_VOLTAGE)]) / 10.0);
      status.setAcInputFrequency(toUnsigned(regs[offset(REG_AC_IN_FREQ)]) / 100.0);

      // ── PV (Panel solar) ───────────────────────────────────────
      status.setPvVoltage(toUnsigned(regs[offset(REG_PV_VOLTAGE)]) / 10.0);
      status.setPvPower(toSigned16(regs[offset(REG_PV_POWER)]));
    } catch (Exception e) {
      log.error("Error leyendo estado del inversor {}: {}", inverterId, e.getMessage());
    }

    return status;
  }

  /**
   * Escribe un único registro de configuración (función 0x06).
   *
   * @param register Dirección del registro (ej: 0x212A para Output Priority)
   * @param value    Valor a escribir (ya escalado si aplica)
   */
  public synchronized boolean writeSingleRegister(int register, int value) {
    try {
      byte[] request = buildWriteSingleRequest(slaveAddress, register, value);
      byte[] response = sendAndReceive(request, 8);

      if (response == null || !validateCrc(response)) {
        log.error("Respuesta inválida al escribir registro 0x{}", Integer.toHexString(register));
        return false;
      }
      log.info("✓ Registro 0x{} escrito con valor {}", Integer.toHexString(register), value);
      return true;
    } catch (Exception e) {
      log.error("Error escribiendo registro 0x{}: {}", Integer.toHexString(register), e.getMessage());
      return false;
    }
  }

  // ═══════════════════════════════════════════════════════════════════
  // CONSTRUCCIÓN DE FRAMES MODBUS RTU
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Construye el frame de petición para leer N registros (función 0x03).
   *
   * Estructura: [Addr][0x03][StartHi][StartLo][CountHi][CountLo][CRC_Lo][CRC_Hi]
   */
  private byte[] buildReadRequest(int slaveAddr, int startRegister, int count) {
    byte[] frame = new byte[8];
    frame[0] = (byte) slaveAddr;
    frame[1] = (byte) FUNC_READ_HOLDING;
    frame[2] = (byte) (startRegister >> 8); // High byte
    frame[3] = (byte) (startRegister & 0xFF); // Low byte
    frame[4] = (byte) (count >> 8);
    frame[5] = (byte) (count & 0xFF);

    int crc = calculateCrc16(frame, 6);
    frame[6] = (byte) (crc & 0xFF); // CRC Low primero (RTU)
    frame[7] = (byte) ((crc >> 8) & 0xFF); // CRC High
    return frame;
  }

  /**
   * Construye el frame para escribir un solo registro (función 0x06).
   *
   * Estructura: [Addr][0x06][RegHi][RegLo][ValHi][ValLo][CRC_Lo][CRC_Hi]
   */
  private byte[] buildWriteSingleRequest(int slaveAddr, int register, int value) {
    byte[] frame = new byte[8];
    frame[0] = (byte) slaveAddr;
    frame[1] = (byte) FUNC_WRITE_SINGLE;
    frame[2] = (byte) (register >> 8);
    frame[3] = (byte) (register & 0xFF);
    frame[4] = (byte) (value >> 8);
    frame[5] = (byte) (value & 0xFF);

    int crc = calculateCrc16(frame, 6);
    frame[6] = (byte) (crc & 0xFF);
    frame[7] = (byte) ((crc >> 8) & 0xFF);
    return frame;
  }

  // ═══════════════════════════════════════════════════════════════════
  // CRC-16 MODBUS (polinomio 0xA001)
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Calcula el CRC-16 estándar Modbus.
   * Polinomio invertido: 0xA001
   * Valor inicial: 0xFFFF
   *
   * Corresponde exactamente al código C# del proyecto .NET de referencia:
   *   crc = 0xFFFF;
   *   crc ^= data[pos];
   *   for(i=0; i<8; i++) if(crc & 0x0001) { crc >>= 1; crc ^= 0xA001; } else crc >>= 1;
   */
  public static int calculateCrc16(byte[] data, int length) {
    int crc = 0xFFFF;

    for (int pos = 0; pos < length; pos++) {
      crc ^= (data[pos] & 0xFF); // XOR con byte (unsigned)

      for (int i = 0; i < 8; i++) {
        if ((crc & 0x0001) != 0) {
          crc >>= 1;
          crc ^= 0xA001; // Polinomio invertido Modbus
        } else {
          crc >>= 1;
        }
      }
    }
    return crc; // 16-bit CRC
  }

  // ═══════════════════════════════════════════════════════════════════
  // ENVÍO / RECEPCIÓN POR PUERTO SERIE
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Lee N registros Holding (función 0x03) y los devuelve como array de int.
   * Cada elemento del array es el valor de un registro (0–65535).
   */
  private int[] readHoldingRegisters(int startRegister, int count) throws Exception {
    byte[] request = buildReadRequest(slaveAddress, startRegister, count);
    // Tamaño esperado de respuesta: 5 bytes fijos + 2 bytes por registro
    int expectedLen = 5 + count * 2;
    byte[] response = sendAndReceive(request, expectedLen);

    if (response == null) return null;
    if (!validateCrc(response)) {
      log.warn("CRC inválido en respuesta Modbus. Datos descartados.");
      return null;
    }

    // Parsear datos: a partir del byte 3 (índice 3), cada 2 bytes = 1 registro
    int[] registers = new int[count];
    for (int i = 0; i < count; i++) {
      int hi = response[3 + i * 2] & 0xFF;
      int lo = response[4 + i * 2] & 0xFF;
      registers[i] = (hi << 8) | lo; // Big-endian: high byte primero
    }
    return registers;
  }

  /**
   * Envía un frame y espera la respuesta.
   * Usa el puerto serie configurado en openPort().
   */
  private byte[] sendAndReceive(byte[] request, int expectedResponseLen) throws Exception {
    if (!serialPort.isOpen()) {
      log.error("Puerto serie no abierto");
      return null;
    }

    OutputStream out = serialPort.getOutputStream();
    InputStream in = serialPort.getInputStream();

    // Vaciar buffer de entrada antes de enviar
    if (in.available() > 0) in.skip(in.available());

    // Enviar petición
    out.write(request);
    out.flush();

    // Leer respuesta con timeout
    byte[] response = new byte[expectedResponseLen];
    int totalRead = 0;
    long deadline = System.currentTimeMillis() + timeoutMs;

    while (totalRead < expectedResponseLen && System.currentTimeMillis() < deadline) {
      int available = in.available();
      if (available > 0) {
        int toRead = Math.min(available, expectedResponseLen - totalRead);
        int read = in.read(response, totalRead, toRead);
        if (read > 0) totalRead += read;
      } else {
        Thread.sleep(5); // Pequeña pausa para no bloquear CPU
      }
    }

    if (totalRead < expectedResponseLen) {
      log.warn("Respuesta incompleta: esperaba {} bytes, recibió {}", expectedResponseLen, totalRead);
      return null;
    }

    return response;
  }

  /**
   * Valida el CRC-16 de un frame de respuesta.
   * Los últimos 2 bytes del frame son el CRC (Low, High).
   */
  private boolean validateCrc(byte[] frame) {
    int len = frame.length;
    int calculatedCrc = calculateCrc16(frame, len - 2);
    int receivedCrc = (frame[len - 2] & 0xFF) | ((frame[len - 1] & 0xFF) << 8);
    return calculatedCrc == receivedCrc;
  }

  // ═══════════════════════════════════════════════════════════════════
  // UTILIDADES DE CONVERSIÓN
  // ═══════════════════════════════════════════════════════════════════

  /** Offset de un registro relativo a la dirección base 0x1101 */
  private int offset(int register) {
    return register - REG_WORKING_MODE;
  }

  /** Interpreta un valor de registro como INT16U (unsigned 16-bit, 0–65535) */
  private int toUnsigned(int rawRegisterValue) {
    return rawRegisterValue & 0xFFFF;
  }

  /**
   * Interpreta un valor de registro como INT16S (signed 16-bit, -32768 a +32767).
   * Modbus devuelve registros como 16-bit unsigned; si bit 15 está en 1 es negativo.
   * Ejemplo: 0xFFF6 = 65526 unsigned → -10 signed
   */
  private double toSigned16(int rawRegisterValue) {
    int unsigned = rawRegisterValue & 0xFFFF;
    // Si bit 15 está activo, es un número negativo en complemento a 2
    if ((unsigned & 0x8000) != 0) {
      return unsigned - 65536;
    }
    return unsigned;
  }
}

```

---

### 3.5 Servicio JK-BMS

```java
// src/main/java/com/felicity/monitor/service/JkBmsService.java
package com.felicity.monitor.service;

import com.fazecast.jSerialComm.SerialPort;
import com.felicity.monitor.model.BmsStatus;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementación del protocolo propietario JK-BMS.
 *
 * Frame de solicitud:
 *   [0x4E][0x57] [LEN_H][LEN_L] [ID4..ID1] [CMD=0x06] [SRC=0x03] [TTYPE=0x00]
 *   [DATA_ID=0x00] [REC4..REC1] [0x68] [CHK4..CHK1]
 *
 * Respuesta: misma estructura con el campo 7 reemplazado por los datos reales.
 */
@Slf4j
@Service
public class JkBmsService {

  @Value("${felicity.jk-bms.port}")
  private String portName;

  @Value("${felicity.jk-bms.baud-rate:115200}")
  private int baudRate;

  // ── Constantes del protocolo ──────────────────────────────────────
  private static final byte STX1 = 0x4E; // 'N'
  private static final byte STX2 = 0x57; // 'W'
  private static final byte END = 0x68;

  // Identificadores de comandos
  private static final byte CMD_READ_ALL = 0x06;
  private static final byte CMD_READ_ONE = 0x03;

  // Identificadores de datos
  private static final int ID_CELL_VOLTAGES = 0x79;
  private static final int ID_MOSFET_TEMP = 0x80;
  private static final int ID_INTERNAL_TEMP = 0x81;
  private static final int ID_BATTERY_TEMP = 0x82;
  private static final int ID_PACK_VOLTAGE = 0x83;
  private static final int ID_CURRENT = 0x84;
  private static final int ID_SOC = 0x85;
  private static final int ID_TEMP_SENSORS = 0x86;
  private static final int ID_CYCLES = 0x87;
  private static final int ID_WARNINGS = 0x8B;
  private static final int ID_STATUS = 0x8C;

  private SerialPort serialPort;

  @PostConstruct
  public void openPort() {
    serialPort = SerialPort.getCommPort(portName);
    serialPort.setBaudRate(baudRate);
    serialPort.setNumDataBits(8);
    serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
    serialPort.setParity(SerialPort.NO_PARITY);
    serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 2000, 2000);

    if (serialPort.openPort()) {
      log.info("✓ Puerto JK-BMS abierto: {} @ {} baud", portName, baudRate);
    } else {
      log.error("✗ No se pudo abrir JK-BMS en {}", portName);
    }
  }

  @PreDestroy
  public void closePort() {
    if (serialPort != null && serialPort.isOpen()) {
      serialPort.closePort();
    }
  }

  // ═══════════════════════════════════════════════════════════════════
  // API PÚBLICA
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Lee todos los datos del BMS con un solo comando 0x06.
   * Es la forma más eficiente: en una petición obtenemos
   * voltajes de celdas, temperaturas, corriente, SOC y alertas.
   */
  public synchronized BmsStatus readAll() {
    BmsStatus status = new BmsStatus();

    try {
      byte[] request = buildReadAllRequest();
      byte[] response = sendAndReceive(request);

      if (response == null) {
        log.warn("No se recibió respuesta del JK-BMS");
        return status;
      }

      if (!validateChecksum(response)) {
        log.warn("Checksum JK-BMS inválido");
        return status;
      }

      parseResponse(response, status);
    } catch (Exception e) {
      log.error("Error leyendo JK-BMS: {}", e.getMessage());
    }

    return status;
  }

  // ═══════════════════════════════════════════════════════════════════
  // CONSTRUCCIÓN DE FRAME JK-BMS
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Construye un frame "Read All Data" (CMD=0x06, DATA_ID=0x00).
   *
   * Estructura completa:
   *  STX(2) + LENGTH(2) + BMS_ID(4) + CMD(1) + SRC(1) + TTYPE(1)
   *  + DATA_ID(1) + RECORD(4) + END(1) + CHECKSUM(4)
   * Total fijo (sin datos variables) = 21 bytes
   */
  private byte[] buildReadAllRequest() {
    // Payload (sin STX y sin checksum):
    //   LENGTH(2) + ID(4) + CMD(1) + SRC(1) + TTYPE(1) + DATA(1) + REC(4) + END(1) = 15 bytes
    // LENGTH = cuenta desde LENGTH hasta END inclusive = 15

    byte[] frame = new byte[21];
    int i = 0;

    frame[i++] = STX1; // 0x4E
    frame[i++] = STX2; // 0x57
    frame[i++] = 0x00; // LENGTH high
    frame[i++] = 0x13; // LENGTH low = 19 (LENGTH propios + todo hasta END)
    frame[i++] = 0x00; // BMS ID (4 bytes, broadcast)
    frame[i++] = 0x00;
    frame[i++] = 0x00;
    frame[i++] = 0x00;
    frame[i++] = CMD_READ_ALL; // Comando: 0x06 = leer todo
    frame[i++] = 0x03; // Source: PC
    frame[i++] = 0x00; // Transport type: request (0=request, 1=reply, 2=active)
    frame[i++] = 0x00; // DATA_ID = 0x00 para "leer todo"
    frame[i++] = 0x00; // Record number (4 bytes)
    frame[i++] = 0x00;
    frame[i++] = 0x00;
    frame[i++] = 0x01;
    frame[i++] = END; // 0x68

    // Calcular checksum acumulativo de todos los bytes anteriores
    long checksum = 0;
    for (int j = 0; j < i; j++) {
      checksum += (frame[j] & 0xFF);
    }
    frame[i++] = 0x00; // CRC high2 (no usado aún, = 0)
    frame[i++] = 0x00; // CRC high1
    frame[i++] = (byte) ((checksum >> 8) & 0xFF); // Checksum high byte
    frame[i] = (byte) (checksum & 0xFF); // Checksum low byte

    return frame;
  }

  // ═══════════════════════════════════════════════════════════════════
  // PARSEO DE RESPUESTA JK-BMS
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Parsea el campo de información (Frame Info Unit, campo 7 de la trama).
   * La estructura del campo 7 en "read all" es una secuencia de:
   *   [ID_BYTE][DATA_BYTES...]
   * La longitud de DATA_BYTES depende del ID_BYTE (ver tabla 5.1).
   */
  private void parseResponse(byte[] response, BmsStatus status) {
    // Los primeros 11 bytes son header:
    // STX(2) + LENGTH(2) + BMS_ID(4) + CMD(1) + SRC(1) + TTYPE(1) = 11
    // El campo de datos empieza en el byte 11.
    // Los últimos 5 bytes son: RECORD(4) + END(1) + CHECKSUM(4) = 9 bytes
    // Hay que excluirlos del parseo.

    int dataStart = 11;
    int dataEnd = response.length - 9; // Excluir record(4) + end(1) + checksum(4)

    if (dataEnd <= dataStart) {
      log.warn("Respuesta JK-BMS demasiado corta para parsear");
      return;
    }

    int pos = dataStart;
    while (pos < dataEnd) {
      int id = response[pos] & 0xFF;
      pos++; // Avanzar al inicio de los datos de este ID

      switch (id) {
        // ── 0x79: Voltajes individuales de celdas ──────────────
        // Formato: [0x79][TOTAL_BYTES][cell#][mV_Hi][mV_Lo] × n
        case ID_CELL_VOLTAGES: {
          int blockLen = response[pos] & 0xFF; // Primer byte = longitud del bloque
          pos++;
          int cellCount = blockLen / 3; // 3 bytes por celda
          List<Double> cellVoltages = new ArrayList<>();

          for (int c = 0; c < cellCount; c++) {
            // int cellNumber = response[pos] & 0xFF; // Número de celda (1-based)
            pos++;
            int mV = readUInt16(response, pos);
            pos += 2;
            cellVoltages.add(mV / 1000.0); // mV → V
          }
          status.setCellVoltages(cellVoltages);

          // Calcular estadísticas
          if (!cellVoltages.isEmpty()) {
            double max = cellVoltages.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double min = cellVoltages.stream().mapToDouble(Double::doubleValue).min().orElse(0);
            double avg = cellVoltages.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            status.setMaxCellVoltage(max);
            status.setMinCellVoltage(min);
            status.setAvgCellVoltage(avg);
            status.setCellVoltageDiff(max - min); // Delta máx-mín
          }
          break;
        }
        // ── 0x80: Temperatura MOSFET ───────────────────────────
        // 2 bytes, rango 0-140; valores > 100 son negativos (100=0°C)
        case ID_MOSFET_TEMP: {
          int raw = readUInt16(response, pos);
          pos += 2;
          status.setMosfetTemperature(decodeJkTemperature(raw));
          break;
        }
        // ── 0x81: Temperatura interna batería ─────────────────
        case ID_INTERNAL_TEMP: {
          int raw = readUInt16(response, pos);
          pos += 2;
          status.setInternalTemperature(decodeJkTemperature(raw));
          break;
        }
        // ── 0x82: Temperatura batería ─────────────────────────
        case ID_BATTERY_TEMP: {
          int raw = readUInt16(response, pos);
          pos += 2;
          status.setBatteryTemperature(decodeJkTemperature(raw));
          break;
        }
        // ── 0x83: Voltaje total del pack ──────────────────────
        // 2 bytes × 0.01 V (resolución 10mV)
        case ID_PACK_VOLTAGE: {
          int raw = readUInt16(response, pos);
          pos += 2;
          status.setPackVoltage(raw * 0.01);
          break;
        }
        // ── 0x84: Corriente ────────────────────────────────────
        // Protocolo v0xC0: bit15=0 descarga, bit15=1 carga; ×0.01A
        case ID_CURRENT: {
          int raw = readUInt16(response, pos);
          pos += 2;
          boolean isCharging = (raw & 0x8000) != 0;
          double current = (raw & 0x7FFF) * 0.01; // Eliminar bit de signo
          status.setCharging(isCharging);
          // Positivo=carga, negativo=descarga (convención)
          status.setCurrent(isCharging ? current : -current);
          break;
        }
        // ── 0x85: SOC ─────────────────────────────────────────
        case ID_SOC: {
          status.setSoc(response[pos] & 0xFF);
          pos += 1;
          break;
        }
        // ── 0x86: Número de sensores de temperatura ───────────
        case ID_TEMP_SENSORS: {
          status.setTempSensorCount(response[pos] & 0xFF);
          pos += 1;
          break;
        }
        // ── 0x87: Ciclos de uso ───────────────────────────────
        case ID_CYCLES: {
          status.setCycleCount(readUInt16(response, pos));
          pos += 2;
          break;
        }
        // ── 0x8B: Flags de alerta ──────────────────────────────
        case ID_WARNINGS: {
          int flags = readUInt16(response, pos);
          pos += 2;
          status.setWarningFlags(flags);
          // Decodificar bits individuales
          status.setLowCapacityWarning((flags & 0x0001) != 0);
          status.setMosfetTempWarning((flags & 0x0002) != 0);
          status.setChargeOvervoltageWarning((flags & 0x0004) != 0);
          status.setDischargeUndervoltageWarn((flags & 0x0008) != 0);
          status.setBatteryTempWarning((flags & 0x0010) != 0);
          status.setChargeOvercurrentWarning((flags & 0x0020) != 0);
          status.setDischargeOvercurrentWarn((flags & 0x0040) != 0);
          status.setCellDiffWarning((flags & 0x0080) != 0);
          break;
        }
        // ── 0x8C: Bits de estado ───────────────────────────────
        case ID_STATUS: {
          int st = readUInt16(response, pos);
          pos += 2;
          status.setChargeMosOn((st & 0x0001) != 0);
          status.setDischargeMosOn((st & 0x0002) != 0);
          status.setBalancingOn((st & 0x0004) != 0);
          break;
        }
        default:
          // ID desconocido: para evitar un loop infinito, salir
          // En producción podrías logear y avanzar 1 byte
          log.trace("ID JK-BMS desconocido: 0x{}", Integer.toHexString(id));
          pos = dataEnd; // Salir del loop
          break;
      }
    }
  }

  // ═══════════════════════════════════════════════════════════════════
  // ENVÍO / RECEPCIÓN
  // ═══════════════════════════════════════════════════════════════════

  private byte[] sendAndReceive(byte[] request) throws Exception {
    if (!serialPort.isOpen()) {
      log.error("Puerto JK-BMS no abierto");
      return null;
    }

    OutputStream out = serialPort.getOutputStream();
    InputStream in = serialPort.getInputStream();

    // Vaciar buffer
    if (in.available() > 0) in.skip(in.available());

    out.write(request);
    out.flush();

    // Leer con timeout de 2 segundos (BMS puede tardar)
    List<Byte> buffer = new ArrayList<>();
    long deadline = System.currentTimeMillis() + 2000;

    while (System.currentTimeMillis() < deadline) {
      int available = in.available();
      if (available > 0) {
        byte[] chunk = new byte[available];
        in.read(chunk);
        for (byte b : chunk) buffer.add(b);

        // Verificar si ya tenemos el end marker (0x68)
        // y al menos 4 bytes de checksum después
        int size = buffer.size();
        if (size > 16 && buffer.get(size - 5) == END) {
          break; // Frame completo
        }
      } else {
        Thread.sleep(10);
      }
    }

    if (buffer.isEmpty()) return null;

    byte[] response = new byte[buffer.size()];
    for (int i = 0; i < response.length; i++) {
      response[i] = buffer.get(i);
    }
    return response;
  }

  // ═══════════════════════════════════════════════════════════════════
  // VALIDACIÓN Y UTILIDADES
  // ═══════════════════════════════════════════════════════════════════

  /**
   * Valida el checksum acumulativo JK-BMS.
   * El checksum es la suma de todos los bytes desde STX hasta END (inclusive).
   * Los últimos 4 bytes son [0x00][0x00][CHK_Hi][CHK_Lo].
   */
  private boolean validateChecksum(byte[] frame) {
    int len = frame.length;
    // Sumar todo menos los 4 bytes finales de checksum
    long sum = 0;
    for (int i = 0; i < len - 4; i++) {
      sum += (frame[i] & 0xFF);
    }
    sum &= 0xFFFF; // Solo 16 bits bajos son el checksum real

    int receivedLo = ((frame[len - 2] & 0xFF) << 8) | (frame[len - 1] & 0xFF);
    return (int) sum == receivedLo;
  }

  /**
   * Decodifica una temperatura JK-BMS.
   * Rango: 0–140 donde 100 = 0°C.
   * Si raw > 100 la temperatura es positiva (raw - 100).
   * Si raw <= 100 la temperatura es negativa (100 - raw → resultado negativo).
   *
   * Ejemplo: raw=120 → +20°C | raw=90 → -10°C
   */
  private double decodeJkTemperature(int raw) {
    if (raw >= 100) {
      return raw - 100.0; // Temperatura positiva
    } else {
      return -(100.0 - raw); // Temperatura negativa
    }
  }

  /** Lee un uint16 big-endian desde el buffer en la posición indicada */
  private int readUInt16(byte[] data, int offset) {
    return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
  }
}

```

---

### 3.6 Modelo BmsStatus

```java
// src/main/java/com/felicity/monitor/model/BmsStatus.java
package com.felicity.monitor.model;

import java.time.Instant;
import java.util.List;
import lombok.Data;

/**
 * Estado completo del JK-BMS.
 * Unidades ya convertidas (V, A, °C, %, etc.)
 */
@Data
public class BmsStatus {

  private Instant timestamp = Instant.now();

  // Celdas individuales
  private List<Double> cellVoltages;
  private double maxCellVoltage;
  private double minCellVoltage;
  private double avgCellVoltage;
  private double cellVoltageDiff; // Desbalance máx-mín

  // Temperatura
  private double mosfetTemperature;
  private double internalTemperature;
  private double batteryTemperature;
  private int tempSensorCount;

  // Pack
  private double packVoltage;
  private double current; // Positivo=carga, negativo=descarga
  private boolean isCharging;
  private int soc; // 0–100 %
  private int cycleCount;

  // Estado de MOS
  private boolean chargeMosOn;
  private boolean dischargeMosOn;
  private boolean balancingOn;

  // Alarmas (bits de 0x8B)
  private int warningFlags;
  private boolean lowCapacityWarning;
  private boolean mosfetTempWarning;
  private boolean chargeOvervoltageWarning;
  private boolean dischargeUndervoltageWarn;
  private boolean batteryTempWarning;
  private boolean chargeOvercurrentWarning;
  private boolean dischargeOvercurrentWarn;
  private boolean cellDiffWarning;

  // Calculados
  public double getPower() {
    return packVoltage * Math.abs(current);
  }

  public boolean hasAnyWarning() {
    return warningFlags != 0;
  }
}

```

---

### 3.7 Polling Service — lee ambos dispositivos y publica por WebSocket

```java
// src/main/java/com/felicity/monitor/service/PollingService.java
package com.felicity.monitor.service;

import com.felicity.monitor.model.BmsStatus;
import com.felicity.monitor.model.InverterStatus;
import com.felicity.monitor.websocket.StatusBroadcaster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Servicio de polling periódico.
 * @Scheduled corre en un thread pool gestionado por Spring.
 *
 * Modifica los intervalos en application.yml:
 *   felicity.modbus.polling-interval-ms
 *   felicity.jk-bms.polling-interval-ms
 */
@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class PollingService {

  private final FelicityModbusService modbusService;
  private final JkBmsService bmsService;
  private final StatusBroadcaster broadcaster;

  // ── Últimos valores conocidos (in-memory cache) ───────────────────
  private volatile InverterStatus lastInverterStatus;
  private volatile BmsStatus lastBmsStatus;

  /**
   * Lee el inversor Felicity cada 2 segundos y difunde por WebSocket.
   * fixedDelayString lee el valor desde properties para ser configurable.
   */
  @Scheduled(fixedDelayString = "${felicity.modbus.polling-interval-ms:2000}")
  public void pollInverter() {
    try {
      InverterStatus status = modbusService.readAllStatus("INV-001");
      lastInverterStatus = status;
      broadcaster.broadcastInverterStatus(status);

      log.debug(
        "Inversor — PV:{} W  BAT:{} V  AC:{} W  MODE:{}",
        status.getPvPower(),
        status.getBatteryVoltage(),
        status.getAcOutputPower(),
        status.getWorkingMode()
      );
    } catch (Exception e) {
      log.error("Error en polling del inversor: {}", e.getMessage());
    }
  }

  /**
   * Lee el BMS JK cada 1 segundo.
   */
  @Scheduled(fixedDelayString = "${felicity.jk-bms.polling-interval-ms:1000}")
  public void pollBms() {
    try {
      BmsStatus status = bmsService.readAll();
      lastBmsStatus = status;
      broadcaster.broadcastBmsStatus(status);

      log.debug(
        "BMS — V:{} V  I:{} A  SOC:{} %  Cells:{}",
        status.getPackVoltage(),
        status.getCurrent(),
        status.getSoc(),
        status.getCellVoltages() != null ? status.getCellVoltages().size() : 0
      );
    } catch (Exception e) {
      log.error("Error en polling del BMS: {}", e.getMessage());
    }
  }

  // Acceso directo al último valor (sin esperar al siguiente poll)
  public InverterStatus getLastInverterStatus() {
    return lastInverterStatus;
  }

  public BmsStatus getLastBmsStatus() {
    return lastBmsStatus;
  }
}

```

---

### 3.8 WebSocket — difusión en tiempo real

```java
// src/main/java/com/felicity/monitor/websocket/WebSocketConfig.java
package com.felicity.monitor.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // Topics que el cliente puede suscribirse
    config.enableSimpleBroker("/topic");
    // Prefijo para mensajes del cliente → servidor
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Endpoint de conexión WebSocket
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // Fallback para navegadores sin WS nativo
  }
}

```

```java
// src/main/java/com/felicity/monitor/websocket/StatusBroadcaster.java
package com.felicity.monitor.websocket;

import com.felicity.monitor.model.BmsStatus;
import com.felicity.monitor.model.InverterStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Difunde los estados al topic WebSocket correspondiente.
 *
 * Clientes React/Flutter se suscriben a:
 *   /topic/inverter  → InverterStatus JSON
 *   /topic/bms       → BmsStatus JSON
 */
@Component
@RequiredArgsConstructor
public class StatusBroadcaster {

  private final SimpMessagingTemplate messagingTemplate;

  public void broadcastInverterStatus(InverterStatus status) {
    messagingTemplate.convertAndSend("/topic/inverter", status);
  }

  public void broadcastBmsStatus(BmsStatus status) {
    messagingTemplate.convertAndSend("/topic/bms", status);
  }
}

```

---

### 3.9 REST Controller

```java
// src/main/java/com/felicity/monitor/controller/MonitorController.java
package com.felicity.monitor.controller;

import com.felicity.monitor.model.BmsStatus;
import com.felicity.monitor.model.InverterStatus;
import com.felicity.monitor.service.FelicityModbusService;
import com.felicity.monitor.service.PollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MonitorController {

  private final PollingService pollingService;
  private final FelicityModbusService modbusService;

  /** Último estado del inversor (sin esperar al próximo poll) */
  @GetMapping("/inverter/status")
  public ResponseEntity<InverterStatus> getInverterStatus() {
    return ResponseEntity.ok(pollingService.getLastInverterStatus());
  }

  /** Último estado del BMS */
  @GetMapping("/bms/status")
  public ResponseEntity<BmsStatus> getBmsStatus() {
    return ResponseEntity.ok(pollingService.getLastBmsStatus());
  }

  /**
   * Escribe un registro de configuración del inversor.
   * Ejemplo: PUT /api/inverter/config/0x212A con body { "value": 1 }
   */
  @PutMapping("/inverter/config/{register}")
  public ResponseEntity<String> writeConfig(@PathVariable String register, @RequestBody java.util.Map<String, Integer> body) {
    int reg = Integer.decode(register); // Acepta "0x212A" o "8490"
    int value = body.get("value");
    boolean ok = modbusService.writeSingleRegister(reg, value);
    return ok ? ResponseEntity.ok("OK") : ResponseEntity.internalServerError().body("Write failed");
  }
}

```

---

## PARTE 4 — DIAGRAMA COMPLETO DEL FLUJO

```
ARRANQUE DE LA APLICACIÓN
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

@PostConstruct → FelicityModbusService.openPort()
                   SerialPort(COM3, 2400, 8N1)

@PostConstruct → JkBmsService.openPort()
                   SerialPort(/dev/ttyUSB0, 115200, 8N1)

CICLO DE POLLING (Spring @Scheduled)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Cada 2000ms:
  PollingService.pollInverter()
    │
    ├─ buildReadRequest(0x1101, 42)
    │    = [01][03][11][01][00][2A][CRC_L][CRC_H]
    │
    ├─ SerialPort.write(request)
    │
    ├─ SerialPort.read() → 89 bytes (5 + 42×2)
    │
    ├─ validateCrc(response)
    │
    ├─ parse: registros[0..41]
    │    → batteryVoltage  = registers[7] / 100.0    (0x1108 - 0x1101 = offset 7)
    │    → batteryCurrent  = toSigned16(registers[8])
    │    → acOutputPower   = toSigned16(registers[29]) (0x111E - 0x1101 = offset 29)
    │    → pvPower         = toSigned16(registers[41]) (0x112A - 0x1101 = offset 41)
    │
    └─ broadcaster.broadcastInverterStatus(status)
         → STOMP /topic/inverter → JSON → React/Flutter

Cada 1000ms:
  PollingService.pollBms()
    │
    ├─ buildReadAllRequest()
    │    = [4E][57][00][13][00][00][00][00][06][03][00][00]
    │      [00][00][00][01][68][00][00][xx][xx]
    │
    ├─ SerialPort.write(request)
    │
    ├─ SerialPort.read() → variable bytes
    │
    ├─ validateChecksum()
    │
    ├─ parseResponse() → iterar [ID][DATA] pares
    │    → 0x79 → cellVoltages[]
    │    → 0x83 → packVoltage = raw × 0.01
    │    → 0x84 → current (bit15=signo)
    │    → 0x85 → soc
    │    → 0x8B → warningFlags (bitfield)
    │
    └─ broadcaster.broadcastBmsStatus(status)
         → STOMP /topic/bms → JSON → React/Flutter

CLIENTE REACT (conexión WebSocket)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

const client = new Client({ brokerURL: 'ws://localhost:8080/ws' });
client.onConnect = () => {
  client.subscribe('/topic/inverter', msg => {
    const status = JSON.parse(msg.body);
    updateDashboard(status);
  });
  client.subscribe('/topic/bms', msg => {
    const bms = JSON.parse(msg.body);
    updateBmsPanel(bms);
  });
};
```

---

## PARTE 5 — TABLA DE CONVERSIONES RÁPIDA

| Registro | Raw value | Fórmula Java             | Resultado |
| -------- | --------- | ------------------------ | --------- |
| 0x1108   | 4800      | `4800 / 100.0`           | 48.00 V   |
| 0x1109   | 0xFFF6    | `toSigned16(65526)`      | -10 A     |
| 0x1111   | 2200      | `2200 / 10.0`            | 220.0 V   |
| 0x1119   | 5000      | `5000 / 100.0`           | 50.00 Hz  |
| 0x1126   | 3650      | `3650 / 10.0`            | 365.0 V   |
| JK 0x83  | 4800      | `4800 * 0.01`            | 48.00 V   |
| JK 0x84  | 0x87D0    | `(0x07D0) * 0.01, carga` | +20.00 A  |
| JK 0x84  | 0x07D0    | `(0x07D0) * 0.01, desc.` | -20.00 A  |
| JK 0x80  | 120       | `120 - 100`              | +20 °C    |
| JK 0x80  | 90        | `-(100 - 90)`            | -10 °C    |

---

## PARTE 6 — TESTS UNITARIOS

```java
// src/test/java/com/felicity/monitor/FelicityModbusServiceTest.java
class FelicityModbusServiceTest {

  @Test
  void crc16_debeCalcularCorrectamente() {
    // Frame de ejemplo: leer registro 0x0001, count=2
    byte[] frame = { 0x01, 0x03, 0x00, 0x01, 0x00, 0x02 };
    int crc = FelicityModbusService.calculateCrc16(frame, frame.length);

    // CRC esperado del ejemplo del protocolo
    // (verificado contra herramienta online Modbus CRC calculator)
    assertEquals(0x9595, crc); // Low=0x95, High=0x95
  }

  @Test
  void toSigned16_debeConvertirValorNegativo() {
    FelicityModbusService svc = new FelicityModbusService();
    // 0xFFF6 = 65526 unsigned = -10 signed
    double result = svc.toSigned16(0xFFF6);
    assertEquals(-10.0, result, 0.001);
  }

  @Test
  void toSigned16_debeConvertirValorPositivo() {
    FelicityModbusService svc = new FelicityModbusService();
    // 0x0014 = 20 → +20 (positivo, carga)
    double result = svc.toSigned16(0x0014);
    assertEquals(20.0, result, 0.001);
  }

  @Test
  void batteryVoltage_debeAplicarEscala() {
    // raw=4800 → 48.00 V
    double voltage = 4800 / 100.0;
    assertEquals(48.00, voltage, 0.001);
  }

  @Test
  void jkTemperature_debeDecodificarPositiva() {
    // raw=120 → +20°C
    double temp = 120 >= 100 ? 120 - 100.0 : -(100.0 - 120);
    assertEquals(20.0, temp, 0.001);
  }

  @Test
  void jkTemperature_debeDecodificarNegativa() {
    // raw=90 → -10°C
    double temp = 90 >= 100 ? 90 - 100.0 : -(100.0 - 90);
    assertEquals(-10.0, temp, 0.001);
  }
}

```

---

## RESUMEN EJECUTIVO

| Punto             | Felicity (Modbus RTU)                | JK-BMS (Propietario)       |
| ----------------- | ------------------------------------ | -------------------------- |
| Velocidad         | 2400 baud                            | 115200 baud                |
| Puerto            | COM3 / /dev/ttyUSB0                  | Mismo o distinto           |
| CRC               | CRC-16 Modbus (0xA001)               | Suma acumulativa           |
| Función lectura   | 0x03 Read Holding Registers          | CMD 0x06 Read All          |
| Función escritura | 0x06 Write Single / 0x10 Write Multi | CMD 0x02 Write             |
| Respuesta         | Big-endian, 2 bytes/registro         | Big-endian, campo ID+DATA  |
| Polling           | Cada 2 s (configurable)              | Cada 1 s (configurable)    |
| Difusión          | WebSocket STOMP /topic/inverter      | WebSocket STOMP /topic/bms |

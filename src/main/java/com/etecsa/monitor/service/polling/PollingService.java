package com.etecsa.monitor.service.polling;

import com.etecsa.monitor.domain.*;
import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.repository.*;
import com.etecsa.monitor.service.alert.AlertEngine;
import com.etecsa.monitor.service.modbus.ModbusTcpClient;
import com.etecsa.monitor.service.websocket.LiveDataBroadcaster;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PollingService — Orquestador Multi-Master.
 *
 * Arquitectura:
 * - 1 hilo ScheduledExecutor por cada Gateway TCP activo
 * - Cada hilo lee los inversores MASTER de ese gateway
 * - Por cada MASTER, lee también sus SLAVES (misma conexión, distinto slaveId)
 * - Los esclavos heredan el MapeoRegistro de la MARCA del master
 * - ConcurrentHashMap追踪 los gateways conectados
 * - Datos leídos → DatoInversor (DB) → LiveDataBroadcaster (WebSocket) → AlertEngine
 *
 * Importante: No usamos @Scheduled fijo porque cada gateway puede tener
 * distinto periodo de muestreo. Usamos ScheduledExecutorService por gateway.
 */
@Service
public class PollingService {

    private static final Logger LOG = LoggerFactory.getLogger(PollingService.class);

    private final GatewayTcpRepository gatewayRepository;
    private final InversorRepository inversorRepository;
    private final MapeoRegistroRepository mapeoRepository;
    private final DatoInversorRepository datoInversorRepository;
    private final ModbusTcpClient modbusClient;
    private final LiveDataBroadcaster broadcaster;
    private final AlertEngine alertEngine;

    /** Gateway ID → ScheduledFuture del hilo de polling */
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> activeTasks = new ConcurrentHashMap<>();

    /** Gateway ID → estado de conexión */
    private final ConcurrentHashMap<Long, EstadoDispositivo> gatewayStates = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public PollingService(
        GatewayTcpRepository gatewayRepository,
        InversorRepository inversorRepository,
        MapeoRegistroRepository mapeoRepository,
        DatoInversorRepository datoInversorRepository,
        ModbusTcpClient modbusClient,
        LiveDataBroadcaster broadcaster,
        AlertEngine alertEngine
    ) {
        this.gatewayRepository = gatewayRepository;
        this.inversorRepository = inversorRepository;
        this.mapeoRepository = mapeoRepository;
        this.datoInversorRepository = datoInversorRepository;
        this.modbusClient = modbusClient;
        this.broadcaster = broadcaster;
        this.alertEngine = alertEngine;
    }

    /**
     * Arranca el polling cuando la aplicación está lista.
     * Descubre gateways activos y lanza un hilo por cada uno.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startPollingOnStartup() {
        LOG.info("=== PollingService: Iniciando discovery de gateways ===");
        discoverAndScheduleGateways();
    }

    /**
     * Rediscovery periódico: cada 60 segundos verifica si hay gateways nuevos o eliminados.
     */
    @Scheduled(fixedRate = 60000)
    public void discoverAndScheduleGateways() {
        List<GatewayTcp> gateways = gatewayRepository.findByActivoTrue();

        // Detener hilos de gateways que ya no existen o están inactivos
        Set<Long> activeGatewayIds = new HashSet<>();
        for (GatewayTcp gw : gateways) {
            activeGatewayIds.add(gw.getId());
        }

        for (Long gwId : new HashSet<>(activeTasks.keySet())) {
            if (!activeGatewayIds.contains(gwId)) {
                stopGatewayPolling(gwId);
            }
        }

        // Lanzar hilos para gateways nuevos
        for (GatewayTcp gw : gateways) {
            if (!activeTasks.containsKey(gw.getId())) {
                startGatewayPolling(gw);
            }
        }

        LOG.debug("PollingService: {} gateways activos monitoreados", activeTasks.size());
    }

    /**
     * Inicia el polling para un gateway específico.
     */
    public void startGatewayPolling(GatewayTcp gateway) {
        long gwId = gateway.getId();
        if (activeTasks.containsKey(gwId)) {
            LOG.warn("Gateway {} ya tiene polling activo", gwId);
            return;
        }

        int intervalSeconds = 10; // default polling interval
        if (gateway.getTiempoEsperaMs() != null && gateway.getTiempoEsperaMs() > 0) {
            intervalSeconds = gateway.getTiempoEsperaMs() / 1000;
        }
        intervalSeconds = Math.max(intervalSeconds, 5); // mínimo 5 segundos

        final int pollInterval = intervalSeconds;

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
            () -> pollGateway(gateway),
            0, // delay inicial
            pollInterval,
            TimeUnit.SECONDS
        );

        activeTasks.put(gwId, future);
        gatewayStates.put(gwId, EstadoDispositivo.Conectado);
        LOG.info("▶ Polling iniciado para gateway '{}' ({}) cada {}s", gateway.getNombre(), gateway.getDireccionIp(), pollInterval);
    }

    /**
     * Detiene el polling de un gateway.
     */
    public void stopGatewayPolling(Long gatewayId) {
        ScheduledFuture<?> future = activeTasks.remove(gatewayId);
        gatewayStates.remove(gatewayId);
        if (future != null) {
            future.cancel(false);
            LOG.info("■ Polling detenido para gateway {}", gatewayId);
        }
    }

    /**
     * Shutdown limpio al parar la aplicación.
     */
    @jakarta.annotation.PreDestroy
    public void shutdown() {
        LOG.info("PollingService: shutdown — deteniendo {} hilos", activeTasks.size());
        activeTasks.values().forEach(f -> f.cancel(false));
        activeTasks.clear();
        scheduler.shutdown();
    }

    // ========== Polling Core ==========

    /**
     * Ejecuta un ciclo de polling para un gateway:
     * 1. Obtiene los inversores activos de ese gateway
     * 2. Para cada MASTER: lee registros, guarda datos, emite WebSocket, evalúa alertas
     * 3. Para cada SLAVE del master: lee registros (misma conexión, distinto slaveId)
     */
    private void pollGateway(GatewayTcp gateway) {
        try {
            String host = gateway.getDireccionIp();
            int port = gateway.getPuerto();

            // Obtener todos los inversores activos de este gateway
            List<Inversor> inversores = inversorRepository.findByGatewayAndActivoTrue(gateway);

            // Separar masters y slaves
            List<Inversor> masters = new ArrayList<>();
            List<Inversor> slaves = new ArrayList<>();
            for (Inversor inv : inversores) {
                if (Boolean.TRUE.equals(inv.getEsMaster())) {
                    masters.add(inv);
                } else {
                    slaves.add(inv);
                }
            }

            int successCount = 0;
            int errorCount = 0;

            // Leer cada MASTER
            for (Inversor master : masters) {
                try {
                    boolean ok = pollInversor(host, port, master);
                    if (ok) successCount++;
                    else errorCount++;
                } catch (Exception e) {
                    LOG.error("Error polling master '{}' (id={}): {}", master.getNombre(), master.getId(), e.getMessage());
                    errorCount++;
                    updateInversorError(master, e.getMessage());
                }

                // Leer los SLAVES de este master (misma conexión TCP, distinto slaveId)
                List<Inversor> masterSlaves = findSlavesOf(master, slaves);
                for (Inversor slave : masterSlaves) {
                    try {
                        boolean ok = pollInversor(host, port, slave);
                        if (ok) successCount++;
                        else errorCount++;
                    } catch (Exception e) {
                        LOG.error("Error polling slave '{}' (id={}): {}", slave.getNombre(), slave.getId(), e.getMessage());
                        errorCount++;
                        updateInversorError(slave, e.getMessage());
                    }
                }
            }

            // Actualizar estado del gateway
            if (errorCount == 0 && successCount > 0) {
                updateGatewayState(gateway, EstadoDispositivo.Conectado);
            } else if (successCount == 0 && errorCount > 0) {
                updateGatewayState(gateway, EstadoDispositivo.Desconectado);
            }
        } catch (Exception e) {
            LOG.error("Error fatal en pollGateway '{}': {}", gateway.getNombre(), e.getMessage());
            updateGatewayState(gateway, EstadoDispositivo.Desconectado);
        }
    }

    /**
     * Lee un inversor individual vía Modbus TCP.
     * 1. Obtiene el MapeoRegistro de su Marca
     * 2. Lee los registros vía ModbusTcpClient
     * 3. Construye un DatoInversor con los datos leídos
     * 4. Guarda en DB
     * 5. Emite por WebSocket
     * 6. Evalúa alertas
     */
    @Transactional
    protected boolean pollInversor(String host, int port, Inversor inversor) {
        // 1. Obtener mapeos de registros para la marca del inversor
        Marca marca = inversor.getMarca();
        if (marca == null) {
            LOG.warn("Inversor '{}' no tiene marca asignada", inversor.getNombre());
            return false;
        }

        List<MapeoRegistro> mapeos = mapeoRepository.findByMarcaAndActivoTrue(marca);
        if (mapeos.isEmpty()) {
            LOG.warn("Marca '{}' no tiene mapeos de registro activos", marca.getNombre());
            return false;
        }

        // 2. Leer vía Modbus TCP
        ModbusTcpClient.ModbusReadResult readResult = modbusClient.readRegistersWithLatency(host, port, inversor.getSlaveId(), mapeos);

        Map<String, Double> raw = readResult.getData();
        if (raw.isEmpty()) {
            LOG.debug("Sin datos de inversor '{}' (slaveId={})", inversor.getNombre(), inversor.getSlaveId());
            return false;
        }

        // 3. Construir DatoInversor
        DatoInversor dato = buildDatoInversor(inversor, raw, readResult.getLatencyMs());

        // 4. Guardar en DB
        datoInversorRepository.save(dato);

        // 5. Actualizar estado del inversor
        updateInversorSuccess(inversor, readResult.getLatencyMs());

        // 6. Emitir por WebSocket
        broadcaster.broadcastDatoInversor(inversor.getId(), dato);

        // 7. Evaluar alertas
        alertEngine.evaluate(inversor, dato);

        return true;
    }

    // ========== Construcción de DatoInversor ==========

    /**
     * Mapea los valores crudos del Modbus a los campos de DatoInversor.
     * Los campos SQL del MapeoRegistro coinciden con los nombres de columna de la tabla.
     */
    private DatoInversor buildDatoInversor(Inversor inversor, Map<String, Double> raw, int latencyMs) {
        DatoInversor dato = new DatoInversor();
        dato.setTiempo(Instant.now());
        dato.setInversor(inversor);
        dato.setLatenciaMs(latencyMs);

        // Mapear cada campo por nombre SQL
        dato.setBateriaVoltajeV(raw.get("bateria_voltaje_v"));
        dato.setBateriaCorrienteA(raw.get("bateria_corriente_a"));
        dato.setBateriaPotenciaW(raw.get("bateria_potencia_w"));
        dato.setPvVoltajeV(raw.get("pv_voltaje_v"));
        dato.setPvPotenciaW(raw.get("pv_potencia_w"));
        dato.setSalidaVoltajeV(raw.get("salida_voltaje_v"));
        dato.setSalidaPotenciaW(raw.get("salida_potencia_w"));
        dato.setSalidaAparenteVa(raw.get("salida_aparente_va"));
        dato.setCargaPorcentaje(raw.get("carga_porcentaje") != null ? raw.get("carga_porcentaje").intValue() : null);
        dato.setRedVoltajeV(raw.get("red_voltaje_v"));
        dato.setRedFrecuenciaHz(raw.get("red_frecuencia_hz"));
        dato.setEficienciaPct(raw.get("eficiencia_pct"));
        dato.setConsumoRedW(raw.get("consumo_red_w"));

        // Flags booleanos derivados de bits de estado
        dato.setBateriaCargando(raw.get("bateria_cargando") != null && raw.get("bateria_cargando") > 0);
        dato.setPvActivo(raw.get("pv_activo") != null && raw.get("pv_activo") > 0);
        dato.setRedActiva(raw.get("red_activa") != null && raw.get("red_activa") > 0);
        dato.setCrcValido(true); // Si llegó hasta acá, el CRC fue válido

        // Modo operación (viene como entero, mapear a string)
        Double modoOpRaw = raw.get("modo_operacion");
        if (modoOpRaw != null) {
            dato.setModoOperacion(decodeModoOperacion(modoOpRaw.intValue()));
        }

        // Código de fallo
        Double falloRaw = raw.get("codigo_fallo");
        if (falloRaw != null) {
            dato.setCodigoFallo(falloRaw.intValue());
        }

        return dato;
    }

    /** Decodifica el modo de operación Felicity Solar */
    private String decodeModoOperacion(int code) {
        switch (code) {
            case 0:
                return "SIN_RED";
            case 1:
                return "RED";
            case 2:
                return "SOLAR";
            case 3:
                return "HIBRIDO";
            case 4:
                return "BATERIA";
            default:
                return "DESCONOCIDO_" + code;
        }
    }

    // ========== Utilidades ==========

    /** Encuentra los slaves que pertenecen a un master */
    private List<Inversor> findSlavesOf(Inversor master, List<Inversor> allSlaves) {
        List<Inversor> result = new ArrayList<>();
        for (Inversor slave : allSlaves) {
            Inversor masterRef = slave.getMasterReferencia();
            if (masterRef != null && masterRef.getId().equals(master.getId())) {
                result.add(slave);
            }
        }
        return result;
    }

    private void updateInversorSuccess(Inversor inversor, int latencyMs) {
        try {
            inversor.setEstadoDispositivo(EstadoDispositivo.Conectado);
            inversor.setUltimaLectura(Instant.now());
            inversor.setUltimaConexion(Instant.now());
            inversor.setUltimoError(null);
            inversorRepository.save(inversor);
        } catch (Exception e) {
            LOG.debug("No se pudo actualizar estado del inversor {}: {}", inversor.getId(), e.getMessage());
        }
    }

    private void updateInversorError(Inversor inversor, String error) {
        try {
            inversor.setEstadoDispositivo(EstadoDispositivo.Desconectado);
            inversor.setUltimoError(truncate(error, 500));
            inversorRepository.save(inversor);
        } catch (Exception e) {
            LOG.debug("No se pudo actualizar error del inversor {}: {}", inversor.getId(), e.getMessage());
        }
    }

    private void updateGatewayState(GatewayTcp gateway, EstadoDispositivo state) {
        EstadoDispositivo previous = gatewayStates.put(gateway.getId(), state);
        if (previous != state) {
            try {
                gateway.setEstadoConexion(state);
                gateway.setUltimaLectura(Instant.now());
                if (state == EstadoDispositivo.Desconectado) {
                    gateway.setUltimoError("Conexion perdida");
                } else {
                    gateway.setUltimoError(null);
                }
                gatewayRepository.save(gateway);
            } catch (Exception e) {
                LOG.debug("No se pudo actualizar estado del gateway {}: {}", gateway.getId(), e.getMessage());
            }
            LOG.info("Gateway '{}' estado → {}", gateway.getNombre(), state);
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return null;
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }

    // ========== API pública ==========

    /** Fuerza un ciclo de polling para un gateway específico */
    public void forcePollNow(Long gatewayId) {
        gatewayRepository
            .findById(gatewayId)
            .ifPresent(gw -> {
                LOG.info("Forzando polling para gateway '{}' ({})", gw.getNombre(), gw.getDireccionIp());
                scheduler.submit(() -> pollGateway(gw));
            });
    }

    /** Devuelve el mapa de estados actuales de los gateways */
    public Map<Long, EstadoDispositivo> getGatewayStates() {
        return Collections.unmodifiableMap(gatewayStates);
    }

    /** Devuelve los IDs de gateways con polling activo */
    public Set<Long> getActiveGatewayIds() {
        return Collections.unmodifiableSet(activeTasks.keySet());
    }
}

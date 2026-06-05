package com.etecsa.monitor.service.alert;

import com.etecsa.monitor.domain.AlertaConfig;
import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.domain.Inversor;
import com.etecsa.monitor.repository.AlertaConfigRepository;
import com.etecsa.monitor.repository.EventoAlertaRepository;
import com.etecsa.monitor.service.websocket.LiveDataBroadcaster;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AlertEngine — Motor de evaluación de alertas.
 *
 * Para cada DatoInversor que llega:
 * 1. Busca AlertaConfig activas para ese inversor
 * 2. Evalúa campo_monitorizado operador umbral
 * 3. Si se cumple Y pasó el cooldown → genera EventoAlerta
 * 4. Emite el evento por WebSocket
 *
 * Operadores soportados: >, <, >=, <=, ==, !=
 */
@Service
public class AlertEngine {

    private static final Logger LOG = LoggerFactory.getLogger(AlertEngine.class);

    private final AlertaConfigRepository alertaConfigRepository;
    private final EventoAlertaRepository eventoAlertaRepository;
    private final LiveDataBroadcaster broadcaster;

    /** Cooldown tracker: "alertaConfigId" → último timestamp de evento */
    private final ConcurrentHashMap<String, Instant> lastAlertTime = new ConcurrentHashMap<>();

    public AlertEngine(
        AlertaConfigRepository alertaConfigRepository,
        EventoAlertaRepository eventoAlertaRepository,
        LiveDataBroadcaster broadcaster
    ) {
        this.alertaConfigRepository = alertaConfigRepository;
        this.eventoAlertaRepository = eventoAlertaRepository;
        this.broadcaster = broadcaster;
    }

    /**
     * Evalúa todas las alertas configuradas para el inversor
     * contra los datos recién leídos.
     */
    @Transactional
    public void evaluate(Inversor inversor, DatoInversor dato) {
        List<AlertaConfig> configs = alertaConfigRepository.findByInversorAndActivaTrue(inversor);

        for (AlertaConfig config : configs) {
            try {
                evaluateOne(config, inversor, dato);
            } catch (Exception e) {
                LOG.error("Error evaluando alerta '{}' para inversor {}: {}", config.getNombre(), inversor.getNombre(), e.getMessage());
            }
        }
    }

    /**
     * Evalúa una AlertaConfig contra un DatoInversor.
     * Si la condición se cumple y pasó el cooldown, genera un EventoAlerta.
     */
    private void evaluateOne(AlertaConfig config, Inversor inversor, DatoInversor dato) {
        // 1. Obtener el valor del campo monitorizado del DatoInversor
        Double valorDetectado = getFieldValue(dato, config.getCampoMonitorizado());
        if (valorDetectado == null) {
            return; // Campo no existe o es null, no podemos evaluar
        }

        // 2. Evaluar la condición: valorDetectado operador umbral
        boolean triggered = evaluateCondition(valorDetectado, config.getOperador(), config.getUmbral());
        if (!triggered) {
            return;
        }

        // 3. Verificar cooldown
        if (isInCooldown(config)) {
            LOG.debug("Alerta '{}' en cooldown, saltando", config.getNombre());
            return;
        }

        // 4. Generar EventoAlerta
        EventoAlerta evento = new EventoAlerta();
        evento.setTimestamp(Instant.now());
        evento.setNombreAlerta(config.getNombre());
        evento.setMensaje(buildMessage(config, inversor, valorDetectado));
        evento.setCampoMonitorizado(config.getCampoMonitorizado());
        evento.setValorDetectado(valorDetectado);
        evento.setUmbral(config.getUmbral());
        evento.setOperador(config.getOperador());
        evento.setNivelSeveridad(config.getNivelSeveridad());
        evento.setNotificacionEnviada(false);
        evento.setCanalUsado(config.getCanalNotificacion());
        evento.setResuelto(false);
        evento.setInversor(inversor);
        evento.setAlertaConfig(config);

        eventoAlertaRepository.save(evento);

        // 5. Actualizar cooldown
        lastAlertTime.put(String.valueOf(config.getId()), Instant.now());

        // 6. Emitir por WebSocket
        broadcaster.broadcastEventoAlerta(inversor.getId(), evento);

        LOG.warn(
            "🚨 ALERTA: {} [inversor={}, campo={}, valor={} {} umbral={}]",
            config.getNombre(),
            inversor.getNombre(),
            config.getCampoMonitorizado(),
            valorDetectado,
            config.getOperador(),
            config.getUmbral()
        );
    }

    /**
     * Obtiene el valor numérico de un campo de DatoInversor por nombre.
     * Usa reflection para mapear campoSql → getter del entity.
     */
    private Double getFieldValue(DatoInversor dato, String campoMonitorizado) {
        try {
            // Mapeo directo de nombres SQL a getters
            switch (campoMonitorizado) {
                case "bateria_voltaje_v":
                    return dato.getBateriaVoltajeV();
                case "bateria_corriente_a":
                    return dato.getBateriaCorrienteA();
                case "bateria_potencia_w":
                    return dato.getBateriaPotenciaW();
                case "pv_voltaje_v":
                    return dato.getPvVoltajeV();
                case "pv_potencia_w":
                    return dato.getPvPotenciaW();
                case "salida_voltaje_v":
                    return dato.getSalidaVoltajeV();
                case "salida_potencia_w":
                    return dato.getSalidaPotenciaW();
                case "salida_aparente_va":
                    return dato.getSalidaAparenteVa();
                case "carga_porcentaje":
                    return dato.getCargaPorcentaje() != null ? dato.getCargaPorcentaje().doubleValue() : null;
                case "red_voltaje_v":
                    return dato.getRedVoltajeV();
                case "red_frecuencia_hz":
                    return dato.getRedFrecuenciaHz();
                case "eficiencia_pct":
                    return dato.getEficienciaPct();
                case "consumo_red_w":
                    return dato.getConsumoRedW();
                case "codigo_fallo":
                    return dato.getCodigoFallo() != null ? dato.getCodigoFallo().doubleValue() : null;
                default:
                    LOG.debug("Campo '{}' no reconocido para evaluación de alertas", campoMonitorizado);
                    return null;
            }
        } catch (Exception e) {
            LOG.warn("Error obteniendo valor del campo '{}': {}", campoMonitorizado, e.getMessage());
            return null;
        }
    }

    /**
     * Evalúa valor operador umbral.
     * Soporta: >, <, >=, <=, ==, !=
     */
    private boolean evaluateCondition(Double valor, String operador, Double umbral) {
        switch (operador.trim()) {
            case ">":
                return valor > umbral;
            case "<":
                return valor < umbral;
            case ">=":
                return valor >= umbral;
            case "<=":
                return valor <= umbral;
            case "==":
                return Math.abs(valor - umbral) < 0.001;
            case "!=":
                return Math.abs(valor - umbral) >= 0.001;
            default:
                LOG.warn("Operador desconocido: '{}'", operador);
                return false;
        }
    }

    /**
     * Verifica si la alerta está en cooldown.
     * Si no hubo evento anterior para esta config, no está en cooldown.
     * Si pasó más del cooldownMinutos desde el último evento, ya salió.
     */
    private boolean isInCooldown(AlertaConfig config) {
        if (config.getCooldownMinutos() == null || config.getCooldownMinutos() <= 0) {
            return false; // Sin cooldown
        }

        Instant lastTime = lastAlertTime.get(String.valueOf(config.getId()));
        if (lastTime == null) {
            return false;
        }

        Instant cooldownEnd = lastTime.plus(config.getCooldownMinutos(), ChronoUnit.MINUTES);
        return Instant.now().isBefore(cooldownEnd);
    }

    /** Construye el mensaje descriptivo del evento */
    private String buildMessage(AlertaConfig config, Inversor inversor, Double valorDetectado) {
        return String.format(
            "%s: %s %s %.2f (umbral: %.2f)",
            inversor.getNombre(),
            config.getCampoMonitorizado(),
            config.getOperador(),
            valorDetectado,
            config.getUmbral()
        );
    }

    /**
     * Limpia entradas de cooldown viejas (cada 30 min).
     * Evita que el mapa crezca indefinidamente.
     */
    public void cleanupCooldowns() {
        Instant cutoff = Instant.now().minus(2, ChronoUnit.HOURS);
        lastAlertTime.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
    }
}

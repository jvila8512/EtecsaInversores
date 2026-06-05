package com.etecsa.monitor.service.websocket;

import com.etecsa.monitor.domain.DatoInversor;
import com.etecsa.monitor.domain.EventoAlerta;
import com.etecsa.monitor.service.dto.DatoInversorDTO;
import com.etecsa.monitor.service.dto.EventoAlertaDTO;
import com.etecsa.monitor.service.mapper.DatoInversorMapper;
import com.etecsa.monitor.service.mapper.EventoAlertaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * LiveDataBroadcaster — Envía datos en tiempo real al frontend vía WebSocket STOMP.
 *
 * Canales (topics):
 *   /topic/inversor/{inversorId}/live   → DatoInversorDTO en tiempo real
 *   /topic/inversor/{inversorId}/alerta  → EventoAlertaDTO cuando se dispara una alerta
 *   /topic/alertas                       → Todas las alertas del sistema
 *
 * JHipster ya configuró WebSocket en WebsocketConfiguration:
 *   - STOMP sobre SockJS en /websocket/tracker
 *   - Broker en /topic
 */
@Service
public class LiveDataBroadcaster {

    private static final Logger LOG = LoggerFactory.getLogger(LiveDataBroadcaster.class);

    private static final String TOPIC_LIVE = "/topic/inversor/%d/live";
    private static final String TOPIC_ALERTA_INVERSOR = "/topic/inversor/%d/alerta";
    private static final String TOPIC_ALERTAS_GLOBAL = "/topic/alertas";

    private final SimpMessagingTemplate messagingTemplate;
    private final DatoInversorMapper datoInversorMapper;
    private final EventoAlertaMapper eventoAlertaMapper;

    public LiveDataBroadcaster(
        SimpMessagingTemplate messagingTemplate,
        DatoInversorMapper datoInversorMapper,
        EventoAlertaMapper eventoAlertaMapper
    ) {
        this.messagingTemplate = messagingTemplate;
        this.datoInversorMapper = datoInversorMapper;
        this.eventoAlertaMapper = eventoAlertaMapper;
    }

    /**
     * Envía un dato de inversor en tiempo real a los suscriptores del canal.
     * El frontend se suscribe a: /topic/inversor/{inversorId}/live
     */
    public void broadcastDatoInversor(Long inversorId, DatoInversor dato) {
        try {
            DatoInversorDTO dto = datoInversorMapper.toDto(dato);
            String destination = String.format(TOPIC_LIVE, inversorId);
            messagingTemplate.convertAndSend(destination, dto);
        } catch (Exception e) {
            LOG.debug("Error broadcasting DatoInversor para inversor {}: {}", inversorId, e.getMessage());
        }
    }

    /**
     * Envía una alerta de inversor a los suscriptores del canal específico
     * Y al canal global de alertas.
     */
    public void broadcastEventoAlerta(Long inversorId, EventoAlerta evento) {
        try {
            EventoAlertaDTO dto = eventoAlertaMapper.toDto(evento);

            // Canal específico del inversor
            String destinationInversor = String.format(TOPIC_ALERTA_INVERSOR, inversorId);
            messagingTemplate.convertAndSend(destinationInversor, dto);

            // Canal global de alertas
            messagingTemplate.convertAndSend(TOPIC_ALERTAS_GLOBAL, dto);
        } catch (Exception e) {
            LOG.debug("Error broadcasting EventoAlerta para inversor {}: {}", inversorId, e.getMessage());
        }
    }
}

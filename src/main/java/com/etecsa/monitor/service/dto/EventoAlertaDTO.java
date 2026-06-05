package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.EventoAlerta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoAlertaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant timestamp;

    @NotNull
    @Size(max = 100)
    private String nombreAlerta;

    @NotNull
    @Size(max = 500)
    private String mensaje;

    @Size(max = 60)
    private String campoMonitorizado;

    private Double valorDetectado;

    private Double umbral;

    @Size(max = 5)
    private String operador;

    @Size(max = 20)
    private String nivelSeveridad;

    private Boolean notificacionEnviada;

    @Size(max = 50)
    private String canalUsado;

    private Boolean resuelto;

    private Instant fechaResolucion;

    @Size(max = 500)
    private String notas;

    private InversorDTO inversor;

    private AlertaConfigDTO alertaConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getNombreAlerta() {
        return nombreAlerta;
    }

    public void setNombreAlerta(String nombreAlerta) {
        this.nombreAlerta = nombreAlerta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCampoMonitorizado() {
        return campoMonitorizado;
    }

    public void setCampoMonitorizado(String campoMonitorizado) {
        this.campoMonitorizado = campoMonitorizado;
    }

    public Double getValorDetectado() {
        return valorDetectado;
    }

    public void setValorDetectado(Double valorDetectado) {
        this.valorDetectado = valorDetectado;
    }

    public Double getUmbral() {
        return umbral;
    }

    public void setUmbral(Double umbral) {
        this.umbral = umbral;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getNivelSeveridad() {
        return nivelSeveridad;
    }

    public void setNivelSeveridad(String nivelSeveridad) {
        this.nivelSeveridad = nivelSeveridad;
    }

    public Boolean getNotificacionEnviada() {
        return notificacionEnviada;
    }

    public void setNotificacionEnviada(Boolean notificacionEnviada) {
        this.notificacionEnviada = notificacionEnviada;
    }

    public String getCanalUsado() {
        return canalUsado;
    }

    public void setCanalUsado(String canalUsado) {
        this.canalUsado = canalUsado;
    }

    public Boolean getResuelto() {
        return resuelto;
    }

    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
    }

    public Instant getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Instant fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public InversorDTO getInversor() {
        return inversor;
    }

    public void setInversor(InversorDTO inversor) {
        this.inversor = inversor;
    }

    public AlertaConfigDTO getAlertaConfig() {
        return alertaConfig;
    }

    public void setAlertaConfig(AlertaConfigDTO alertaConfig) {
        this.alertaConfig = alertaConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventoAlertaDTO)) {
            return false;
        }

        EventoAlertaDTO eventoAlertaDTO = (EventoAlertaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventoAlertaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoAlertaDTO{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", nombreAlerta='" + getNombreAlerta() + "'" +
            ", mensaje='" + getMensaje() + "'" +
            ", campoMonitorizado='" + getCampoMonitorizado() + "'" +
            ", valorDetectado=" + getValorDetectado() +
            ", umbral=" + getUmbral() +
            ", operador='" + getOperador() + "'" +
            ", nivelSeveridad='" + getNivelSeveridad() + "'" +
            ", notificacionEnviada='" + getNotificacionEnviada() + "'" +
            ", canalUsado='" + getCanalUsado() + "'" +
            ", resuelto='" + getResuelto() + "'" +
            ", fechaResolucion='" + getFechaResolucion() + "'" +
            ", notas='" + getNotas() + "'" +
            ", inversor=" + getInversor() +
            ", alertaConfig=" + getAlertaConfig() +
            "}";
    }
}

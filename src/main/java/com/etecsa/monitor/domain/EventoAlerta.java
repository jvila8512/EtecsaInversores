package com.etecsa.monitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventoAlerta.
 */
@Entity
@Table(name = "evento_alerta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoAlerta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_alerta", length = 100, nullable = false)
    private String nombreAlerta;

    @NotNull
    @Size(max = 500)
    @Column(name = "mensaje", length = 500, nullable = false)
    private String mensaje;

    @Size(max = 60)
    @Column(name = "campo_monitorizado", length = 60)
    private String campoMonitorizado;

    @Column(name = "valor_detectado")
    private Double valorDetectado;

    @Column(name = "umbral")
    private Double umbral;

    @Size(max = 5)
    @Column(name = "operador", length = 5)
    private String operador;

    @Size(max = 20)
    @Column(name = "nivel_severidad", length = 20)
    private String nivelSeveridad;

    @Column(name = "notificacion_enviada")
    private Boolean notificacionEnviada;

    @Size(max = 50)
    @Column(name = "canal_usado", length = 50)
    private String canalUsado;

    @Column(name = "resuelto")
    private Boolean resuelto;

    @Column(name = "fecha_resolucion")
    private Instant fechaResolucion;

    @Size(max = 500)
    @Column(name = "notas", length = 500)
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio", "marca", "modelo", "gateway", "masterReferencia" }, allowSetters = true)
    private Inversor inversor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inversor" }, allowSetters = true)
    private AlertaConfig alertaConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventoAlerta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public EventoAlerta timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getNombreAlerta() {
        return this.nombreAlerta;
    }

    public EventoAlerta nombreAlerta(String nombreAlerta) {
        this.setNombreAlerta(nombreAlerta);
        return this;
    }

    public void setNombreAlerta(String nombreAlerta) {
        this.nombreAlerta = nombreAlerta;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public EventoAlerta mensaje(String mensaje) {
        this.setMensaje(mensaje);
        return this;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCampoMonitorizado() {
        return this.campoMonitorizado;
    }

    public EventoAlerta campoMonitorizado(String campoMonitorizado) {
        this.setCampoMonitorizado(campoMonitorizado);
        return this;
    }

    public void setCampoMonitorizado(String campoMonitorizado) {
        this.campoMonitorizado = campoMonitorizado;
    }

    public Double getValorDetectado() {
        return this.valorDetectado;
    }

    public EventoAlerta valorDetectado(Double valorDetectado) {
        this.setValorDetectado(valorDetectado);
        return this;
    }

    public void setValorDetectado(Double valorDetectado) {
        this.valorDetectado = valorDetectado;
    }

    public Double getUmbral() {
        return this.umbral;
    }

    public EventoAlerta umbral(Double umbral) {
        this.setUmbral(umbral);
        return this;
    }

    public void setUmbral(Double umbral) {
        this.umbral = umbral;
    }

    public String getOperador() {
        return this.operador;
    }

    public EventoAlerta operador(String operador) {
        this.setOperador(operador);
        return this;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getNivelSeveridad() {
        return this.nivelSeveridad;
    }

    public EventoAlerta nivelSeveridad(String nivelSeveridad) {
        this.setNivelSeveridad(nivelSeveridad);
        return this;
    }

    public void setNivelSeveridad(String nivelSeveridad) {
        this.nivelSeveridad = nivelSeveridad;
    }

    public Boolean getNotificacionEnviada() {
        return this.notificacionEnviada;
    }

    public EventoAlerta notificacionEnviada(Boolean notificacionEnviada) {
        this.setNotificacionEnviada(notificacionEnviada);
        return this;
    }

    public void setNotificacionEnviada(Boolean notificacionEnviada) {
        this.notificacionEnviada = notificacionEnviada;
    }

    public String getCanalUsado() {
        return this.canalUsado;
    }

    public EventoAlerta canalUsado(String canalUsado) {
        this.setCanalUsado(canalUsado);
        return this;
    }

    public void setCanalUsado(String canalUsado) {
        this.canalUsado = canalUsado;
    }

    public Boolean getResuelto() {
        return this.resuelto;
    }

    public EventoAlerta resuelto(Boolean resuelto) {
        this.setResuelto(resuelto);
        return this;
    }

    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
    }

    public Instant getFechaResolucion() {
        return this.fechaResolucion;
    }

    public EventoAlerta fechaResolucion(Instant fechaResolucion) {
        this.setFechaResolucion(fechaResolucion);
        return this;
    }

    public void setFechaResolucion(Instant fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getNotas() {
        return this.notas;
    }

    public EventoAlerta notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Inversor getInversor() {
        return this.inversor;
    }

    public void setInversor(Inversor inversor) {
        this.inversor = inversor;
    }

    public EventoAlerta inversor(Inversor inversor) {
        this.setInversor(inversor);
        return this;
    }

    public AlertaConfig getAlertaConfig() {
        return this.alertaConfig;
    }

    public void setAlertaConfig(AlertaConfig alertaConfig) {
        this.alertaConfig = alertaConfig;
    }

    public EventoAlerta alertaConfig(AlertaConfig alertaConfig) {
        this.setAlertaConfig(alertaConfig);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventoAlerta)) {
            return false;
        }
        return getId() != null && getId().equals(((EventoAlerta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoAlerta{" +
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
            "}";
    }
}

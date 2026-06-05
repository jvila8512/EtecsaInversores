package com.etecsa.monitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlertaConfig.
 */
@Entity
@Table(name = "alerta_config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertaConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @NotNull
    @Size(max = 60)
    @Column(name = "campo_monitorizado", length = 60, nullable = false)
    private String campoMonitorizado;

    @NotNull
    @Size(max = 5)
    @Column(name = "operador", length = 5, nullable = false)
    private String operador;

    @NotNull
    @Column(name = "umbral", nullable = false)
    private Double umbral;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Size(max = 20)
    @Column(name = "nivel_severidad", length = 20)
    private String nivelSeveridad;

    @Size(max = 50)
    @Column(name = "canal_notificacion", length = 50)
    private String canalNotificacion;

    @Column(name = "cooldown_minutos")
    private Integer cooldownMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio", "marca", "modelo", "gateway", "masterReferencia" }, allowSetters = true)
    private Inversor inversor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlertaConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public AlertaConfig nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public AlertaConfig descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCampoMonitorizado() {
        return this.campoMonitorizado;
    }

    public AlertaConfig campoMonitorizado(String campoMonitorizado) {
        this.setCampoMonitorizado(campoMonitorizado);
        return this;
    }

    public void setCampoMonitorizado(String campoMonitorizado) {
        this.campoMonitorizado = campoMonitorizado;
    }

    public String getOperador() {
        return this.operador;
    }

    public AlertaConfig operador(String operador) {
        this.setOperador(operador);
        return this;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Double getUmbral() {
        return this.umbral;
    }

    public AlertaConfig umbral(Double umbral) {
        this.setUmbral(umbral);
        return this;
    }

    public void setUmbral(Double umbral) {
        this.umbral = umbral;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public AlertaConfig activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getNivelSeveridad() {
        return this.nivelSeveridad;
    }

    public AlertaConfig nivelSeveridad(String nivelSeveridad) {
        this.setNivelSeveridad(nivelSeveridad);
        return this;
    }

    public void setNivelSeveridad(String nivelSeveridad) {
        this.nivelSeveridad = nivelSeveridad;
    }

    public String getCanalNotificacion() {
        return this.canalNotificacion;
    }

    public AlertaConfig canalNotificacion(String canalNotificacion) {
        this.setCanalNotificacion(canalNotificacion);
        return this;
    }

    public void setCanalNotificacion(String canalNotificacion) {
        this.canalNotificacion = canalNotificacion;
    }

    public Integer getCooldownMinutos() {
        return this.cooldownMinutos;
    }

    public AlertaConfig cooldownMinutos(Integer cooldownMinutos) {
        this.setCooldownMinutos(cooldownMinutos);
        return this;
    }

    public void setCooldownMinutos(Integer cooldownMinutos) {
        this.cooldownMinutos = cooldownMinutos;
    }

    public Inversor getInversor() {
        return this.inversor;
    }

    public void setInversor(Inversor inversor) {
        this.inversor = inversor;
    }

    public AlertaConfig inversor(Inversor inversor) {
        this.setInversor(inversor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertaConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((AlertaConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertaConfig{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", campoMonitorizado='" + getCampoMonitorizado() + "'" +
            ", operador='" + getOperador() + "'" +
            ", umbral=" + getUmbral() +
            ", activa='" + getActiva() + "'" +
            ", nivelSeveridad='" + getNivelSeveridad() + "'" +
            ", canalNotificacion='" + getCanalNotificacion() + "'" +
            ", cooldownMinutos=" + getCooldownMinutos() +
            "}";
    }
}

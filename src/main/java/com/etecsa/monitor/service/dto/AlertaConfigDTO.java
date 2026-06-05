package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.AlertaConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlertaConfigDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    @Size(max = 60)
    private String campoMonitorizado;

    @NotNull
    @Size(max = 5)
    private String operador;

    @NotNull
    private Double umbral;

    @NotNull
    private Boolean activa;

    @Size(max = 20)
    private String nivelSeveridad;

    @Size(max = 50)
    private String canalNotificacion;

    private Integer cooldownMinutos;

    private InversorDTO inversor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCampoMonitorizado() {
        return campoMonitorizado;
    }

    public void setCampoMonitorizado(String campoMonitorizado) {
        this.campoMonitorizado = campoMonitorizado;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Double getUmbral() {
        return umbral;
    }

    public void setUmbral(Double umbral) {
        this.umbral = umbral;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getNivelSeveridad() {
        return nivelSeveridad;
    }

    public void setNivelSeveridad(String nivelSeveridad) {
        this.nivelSeveridad = nivelSeveridad;
    }

    public String getCanalNotificacion() {
        return canalNotificacion;
    }

    public void setCanalNotificacion(String canalNotificacion) {
        this.canalNotificacion = canalNotificacion;
    }

    public Integer getCooldownMinutos() {
        return cooldownMinutos;
    }

    public void setCooldownMinutos(Integer cooldownMinutos) {
        this.cooldownMinutos = cooldownMinutos;
    }

    public InversorDTO getInversor() {
        return inversor;
    }

    public void setInversor(InversorDTO inversor) {
        this.inversor = inversor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertaConfigDTO)) {
            return false;
        }

        AlertaConfigDTO alertaConfigDTO = (AlertaConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alertaConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlertaConfigDTO{" +
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
            ", inversor=" + getInversor() +
            "}";
    }
}

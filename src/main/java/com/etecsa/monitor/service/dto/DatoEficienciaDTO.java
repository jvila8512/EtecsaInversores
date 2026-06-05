package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.DatoEficiencia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoEficienciaDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate fecha;

    private Double energiaPvWh;

    private Double energiaCargaWh;

    private Double energiaRedWh;

    private Double eficienciaPromPct;

    private Double horasSol;

    private Double picoPvW;

    private Double picoCargaW;

    private Double maxBateriaV;

    private Double minBateriaV;

    private Integer totalLecturas;

    private Instant calculadoEn;

    private InversorDTO inversor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getEnergiaPvWh() {
        return energiaPvWh;
    }

    public void setEnergiaPvWh(Double energiaPvWh) {
        this.energiaPvWh = energiaPvWh;
    }

    public Double getEnergiaCargaWh() {
        return energiaCargaWh;
    }

    public void setEnergiaCargaWh(Double energiaCargaWh) {
        this.energiaCargaWh = energiaCargaWh;
    }

    public Double getEnergiaRedWh() {
        return energiaRedWh;
    }

    public void setEnergiaRedWh(Double energiaRedWh) {
        this.energiaRedWh = energiaRedWh;
    }

    public Double getEficienciaPromPct() {
        return eficienciaPromPct;
    }

    public void setEficienciaPromPct(Double eficienciaPromPct) {
        this.eficienciaPromPct = eficienciaPromPct;
    }

    public Double getHorasSol() {
        return horasSol;
    }

    public void setHorasSol(Double horasSol) {
        this.horasSol = horasSol;
    }

    public Double getPicoPvW() {
        return picoPvW;
    }

    public void setPicoPvW(Double picoPvW) {
        this.picoPvW = picoPvW;
    }

    public Double getPicoCargaW() {
        return picoCargaW;
    }

    public void setPicoCargaW(Double picoCargaW) {
        this.picoCargaW = picoCargaW;
    }

    public Double getMaxBateriaV() {
        return maxBateriaV;
    }

    public void setMaxBateriaV(Double maxBateriaV) {
        this.maxBateriaV = maxBateriaV;
    }

    public Double getMinBateriaV() {
        return minBateriaV;
    }

    public void setMinBateriaV(Double minBateriaV) {
        this.minBateriaV = minBateriaV;
    }

    public Integer getTotalLecturas() {
        return totalLecturas;
    }

    public void setTotalLecturas(Integer totalLecturas) {
        this.totalLecturas = totalLecturas;
    }

    public Instant getCalculadoEn() {
        return calculadoEn;
    }

    public void setCalculadoEn(Instant calculadoEn) {
        this.calculadoEn = calculadoEn;
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
        if (!(o instanceof DatoEficienciaDTO)) {
            return false;
        }

        DatoEficienciaDTO datoEficienciaDTO = (DatoEficienciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, datoEficienciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoEficienciaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", energiaPvWh=" + getEnergiaPvWh() +
            ", energiaCargaWh=" + getEnergiaCargaWh() +
            ", energiaRedWh=" + getEnergiaRedWh() +
            ", eficienciaPromPct=" + getEficienciaPromPct() +
            ", horasSol=" + getHorasSol() +
            ", picoPvW=" + getPicoPvW() +
            ", picoCargaW=" + getPicoCargaW() +
            ", maxBateriaV=" + getMaxBateriaV() +
            ", minBateriaV=" + getMinBateriaV() +
            ", totalLecturas=" + getTotalLecturas() +
            ", calculadoEn='" + getCalculadoEn() + "'" +
            ", inversor=" + getInversor() +
            "}";
    }
}

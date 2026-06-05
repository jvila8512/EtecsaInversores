package com.etecsa.monitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DatoEficiencia.
 */
@Entity
@Table(name = "dato_eficiencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoEficiencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "energia_pv_wh")
    private Double energiaPvWh;

    @Column(name = "energia_carga_wh")
    private Double energiaCargaWh;

    @Column(name = "energia_red_wh")
    private Double energiaRedWh;

    @Column(name = "eficiencia_prom_pct")
    private Double eficienciaPromPct;

    @Column(name = "horas_sol")
    private Double horasSol;

    @Column(name = "pico_pv_w")
    private Double picoPvW;

    @Column(name = "pico_carga_w")
    private Double picoCargaW;

    @Column(name = "max_bateria_v")
    private Double maxBateriaV;

    @Column(name = "min_bateria_v")
    private Double minBateriaV;

    @Column(name = "total_lecturas")
    private Integer totalLecturas;

    @Column(name = "calculado_en")
    private Instant calculadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio", "marca", "modelo", "gateway", "masterReferencia" }, allowSetters = true)
    private Inversor inversor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DatoEficiencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public DatoEficiencia fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getEnergiaPvWh() {
        return this.energiaPvWh;
    }

    public DatoEficiencia energiaPvWh(Double energiaPvWh) {
        this.setEnergiaPvWh(energiaPvWh);
        return this;
    }

    public void setEnergiaPvWh(Double energiaPvWh) {
        this.energiaPvWh = energiaPvWh;
    }

    public Double getEnergiaCargaWh() {
        return this.energiaCargaWh;
    }

    public DatoEficiencia energiaCargaWh(Double energiaCargaWh) {
        this.setEnergiaCargaWh(energiaCargaWh);
        return this;
    }

    public void setEnergiaCargaWh(Double energiaCargaWh) {
        this.energiaCargaWh = energiaCargaWh;
    }

    public Double getEnergiaRedWh() {
        return this.energiaRedWh;
    }

    public DatoEficiencia energiaRedWh(Double energiaRedWh) {
        this.setEnergiaRedWh(energiaRedWh);
        return this;
    }

    public void setEnergiaRedWh(Double energiaRedWh) {
        this.energiaRedWh = energiaRedWh;
    }

    public Double getEficienciaPromPct() {
        return this.eficienciaPromPct;
    }

    public DatoEficiencia eficienciaPromPct(Double eficienciaPromPct) {
        this.setEficienciaPromPct(eficienciaPromPct);
        return this;
    }

    public void setEficienciaPromPct(Double eficienciaPromPct) {
        this.eficienciaPromPct = eficienciaPromPct;
    }

    public Double getHorasSol() {
        return this.horasSol;
    }

    public DatoEficiencia horasSol(Double horasSol) {
        this.setHorasSol(horasSol);
        return this;
    }

    public void setHorasSol(Double horasSol) {
        this.horasSol = horasSol;
    }

    public Double getPicoPvW() {
        return this.picoPvW;
    }

    public DatoEficiencia picoPvW(Double picoPvW) {
        this.setPicoPvW(picoPvW);
        return this;
    }

    public void setPicoPvW(Double picoPvW) {
        this.picoPvW = picoPvW;
    }

    public Double getPicoCargaW() {
        return this.picoCargaW;
    }

    public DatoEficiencia picoCargaW(Double picoCargaW) {
        this.setPicoCargaW(picoCargaW);
        return this;
    }

    public void setPicoCargaW(Double picoCargaW) {
        this.picoCargaW = picoCargaW;
    }

    public Double getMaxBateriaV() {
        return this.maxBateriaV;
    }

    public DatoEficiencia maxBateriaV(Double maxBateriaV) {
        this.setMaxBateriaV(maxBateriaV);
        return this;
    }

    public void setMaxBateriaV(Double maxBateriaV) {
        this.maxBateriaV = maxBateriaV;
    }

    public Double getMinBateriaV() {
        return this.minBateriaV;
    }

    public DatoEficiencia minBateriaV(Double minBateriaV) {
        this.setMinBateriaV(minBateriaV);
        return this;
    }

    public void setMinBateriaV(Double minBateriaV) {
        this.minBateriaV = minBateriaV;
    }

    public Integer getTotalLecturas() {
        return this.totalLecturas;
    }

    public DatoEficiencia totalLecturas(Integer totalLecturas) {
        this.setTotalLecturas(totalLecturas);
        return this;
    }

    public void setTotalLecturas(Integer totalLecturas) {
        this.totalLecturas = totalLecturas;
    }

    public Instant getCalculadoEn() {
        return this.calculadoEn;
    }

    public DatoEficiencia calculadoEn(Instant calculadoEn) {
        this.setCalculadoEn(calculadoEn);
        return this;
    }

    public void setCalculadoEn(Instant calculadoEn) {
        this.calculadoEn = calculadoEn;
    }

    public Inversor getInversor() {
        return this.inversor;
    }

    public void setInversor(Inversor inversor) {
        this.inversor = inversor;
    }

    public DatoEficiencia inversor(Inversor inversor) {
        this.setInversor(inversor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatoEficiencia)) {
            return false;
        }
        return getId() != null && getId().equals(((DatoEficiencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoEficiencia{" +
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
            "}";
    }
}

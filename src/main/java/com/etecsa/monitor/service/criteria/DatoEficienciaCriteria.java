package com.etecsa.monitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.DatoEficiencia} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.DatoEficienciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dato-eficiencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoEficienciaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private DoubleFilter energiaPvWh;

    private DoubleFilter energiaCargaWh;

    private DoubleFilter energiaRedWh;

    private DoubleFilter eficienciaPromPct;

    private DoubleFilter horasSol;

    private DoubleFilter picoPvW;

    private DoubleFilter picoCargaW;

    private DoubleFilter maxBateriaV;

    private DoubleFilter minBateriaV;

    private IntegerFilter totalLecturas;

    private InstantFilter calculadoEn;

    private LongFilter inversorId;

    private Boolean distinct;

    public DatoEficienciaCriteria() {}

    public DatoEficienciaCriteria(DatoEficienciaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(LocalDateFilter::copy).orElse(null);
        this.energiaPvWh = other.optionalEnergiaPvWh().map(DoubleFilter::copy).orElse(null);
        this.energiaCargaWh = other.optionalEnergiaCargaWh().map(DoubleFilter::copy).orElse(null);
        this.energiaRedWh = other.optionalEnergiaRedWh().map(DoubleFilter::copy).orElse(null);
        this.eficienciaPromPct = other.optionalEficienciaPromPct().map(DoubleFilter::copy).orElse(null);
        this.horasSol = other.optionalHorasSol().map(DoubleFilter::copy).orElse(null);
        this.picoPvW = other.optionalPicoPvW().map(DoubleFilter::copy).orElse(null);
        this.picoCargaW = other.optionalPicoCargaW().map(DoubleFilter::copy).orElse(null);
        this.maxBateriaV = other.optionalMaxBateriaV().map(DoubleFilter::copy).orElse(null);
        this.minBateriaV = other.optionalMinBateriaV().map(DoubleFilter::copy).orElse(null);
        this.totalLecturas = other.optionalTotalLecturas().map(IntegerFilter::copy).orElse(null);
        this.calculadoEn = other.optionalCalculadoEn().map(InstantFilter::copy).orElse(null);
        this.inversorId = other.optionalInversorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DatoEficienciaCriteria copy() {
        return new DatoEficienciaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public Optional<LocalDateFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            setFecha(new LocalDateFilter());
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public DoubleFilter getEnergiaPvWh() {
        return energiaPvWh;
    }

    public Optional<DoubleFilter> optionalEnergiaPvWh() {
        return Optional.ofNullable(energiaPvWh);
    }

    public DoubleFilter energiaPvWh() {
        if (energiaPvWh == null) {
            setEnergiaPvWh(new DoubleFilter());
        }
        return energiaPvWh;
    }

    public void setEnergiaPvWh(DoubleFilter energiaPvWh) {
        this.energiaPvWh = energiaPvWh;
    }

    public DoubleFilter getEnergiaCargaWh() {
        return energiaCargaWh;
    }

    public Optional<DoubleFilter> optionalEnergiaCargaWh() {
        return Optional.ofNullable(energiaCargaWh);
    }

    public DoubleFilter energiaCargaWh() {
        if (energiaCargaWh == null) {
            setEnergiaCargaWh(new DoubleFilter());
        }
        return energiaCargaWh;
    }

    public void setEnergiaCargaWh(DoubleFilter energiaCargaWh) {
        this.energiaCargaWh = energiaCargaWh;
    }

    public DoubleFilter getEnergiaRedWh() {
        return energiaRedWh;
    }

    public Optional<DoubleFilter> optionalEnergiaRedWh() {
        return Optional.ofNullable(energiaRedWh);
    }

    public DoubleFilter energiaRedWh() {
        if (energiaRedWh == null) {
            setEnergiaRedWh(new DoubleFilter());
        }
        return energiaRedWh;
    }

    public void setEnergiaRedWh(DoubleFilter energiaRedWh) {
        this.energiaRedWh = energiaRedWh;
    }

    public DoubleFilter getEficienciaPromPct() {
        return eficienciaPromPct;
    }

    public Optional<DoubleFilter> optionalEficienciaPromPct() {
        return Optional.ofNullable(eficienciaPromPct);
    }

    public DoubleFilter eficienciaPromPct() {
        if (eficienciaPromPct == null) {
            setEficienciaPromPct(new DoubleFilter());
        }
        return eficienciaPromPct;
    }

    public void setEficienciaPromPct(DoubleFilter eficienciaPromPct) {
        this.eficienciaPromPct = eficienciaPromPct;
    }

    public DoubleFilter getHorasSol() {
        return horasSol;
    }

    public Optional<DoubleFilter> optionalHorasSol() {
        return Optional.ofNullable(horasSol);
    }

    public DoubleFilter horasSol() {
        if (horasSol == null) {
            setHorasSol(new DoubleFilter());
        }
        return horasSol;
    }

    public void setHorasSol(DoubleFilter horasSol) {
        this.horasSol = horasSol;
    }

    public DoubleFilter getPicoPvW() {
        return picoPvW;
    }

    public Optional<DoubleFilter> optionalPicoPvW() {
        return Optional.ofNullable(picoPvW);
    }

    public DoubleFilter picoPvW() {
        if (picoPvW == null) {
            setPicoPvW(new DoubleFilter());
        }
        return picoPvW;
    }

    public void setPicoPvW(DoubleFilter picoPvW) {
        this.picoPvW = picoPvW;
    }

    public DoubleFilter getPicoCargaW() {
        return picoCargaW;
    }

    public Optional<DoubleFilter> optionalPicoCargaW() {
        return Optional.ofNullable(picoCargaW);
    }

    public DoubleFilter picoCargaW() {
        if (picoCargaW == null) {
            setPicoCargaW(new DoubleFilter());
        }
        return picoCargaW;
    }

    public void setPicoCargaW(DoubleFilter picoCargaW) {
        this.picoCargaW = picoCargaW;
    }

    public DoubleFilter getMaxBateriaV() {
        return maxBateriaV;
    }

    public Optional<DoubleFilter> optionalMaxBateriaV() {
        return Optional.ofNullable(maxBateriaV);
    }

    public DoubleFilter maxBateriaV() {
        if (maxBateriaV == null) {
            setMaxBateriaV(new DoubleFilter());
        }
        return maxBateriaV;
    }

    public void setMaxBateriaV(DoubleFilter maxBateriaV) {
        this.maxBateriaV = maxBateriaV;
    }

    public DoubleFilter getMinBateriaV() {
        return minBateriaV;
    }

    public Optional<DoubleFilter> optionalMinBateriaV() {
        return Optional.ofNullable(minBateriaV);
    }

    public DoubleFilter minBateriaV() {
        if (minBateriaV == null) {
            setMinBateriaV(new DoubleFilter());
        }
        return minBateriaV;
    }

    public void setMinBateriaV(DoubleFilter minBateriaV) {
        this.minBateriaV = minBateriaV;
    }

    public IntegerFilter getTotalLecturas() {
        return totalLecturas;
    }

    public Optional<IntegerFilter> optionalTotalLecturas() {
        return Optional.ofNullable(totalLecturas);
    }

    public IntegerFilter totalLecturas() {
        if (totalLecturas == null) {
            setTotalLecturas(new IntegerFilter());
        }
        return totalLecturas;
    }

    public void setTotalLecturas(IntegerFilter totalLecturas) {
        this.totalLecturas = totalLecturas;
    }

    public InstantFilter getCalculadoEn() {
        return calculadoEn;
    }

    public Optional<InstantFilter> optionalCalculadoEn() {
        return Optional.ofNullable(calculadoEn);
    }

    public InstantFilter calculadoEn() {
        if (calculadoEn == null) {
            setCalculadoEn(new InstantFilter());
        }
        return calculadoEn;
    }

    public void setCalculadoEn(InstantFilter calculadoEn) {
        this.calculadoEn = calculadoEn;
    }

    public LongFilter getInversorId() {
        return inversorId;
    }

    public Optional<LongFilter> optionalInversorId() {
        return Optional.ofNullable(inversorId);
    }

    public LongFilter inversorId() {
        if (inversorId == null) {
            setInversorId(new LongFilter());
        }
        return inversorId;
    }

    public void setInversorId(LongFilter inversorId) {
        this.inversorId = inversorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DatoEficienciaCriteria that = (DatoEficienciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(energiaPvWh, that.energiaPvWh) &&
            Objects.equals(energiaCargaWh, that.energiaCargaWh) &&
            Objects.equals(energiaRedWh, that.energiaRedWh) &&
            Objects.equals(eficienciaPromPct, that.eficienciaPromPct) &&
            Objects.equals(horasSol, that.horasSol) &&
            Objects.equals(picoPvW, that.picoPvW) &&
            Objects.equals(picoCargaW, that.picoCargaW) &&
            Objects.equals(maxBateriaV, that.maxBateriaV) &&
            Objects.equals(minBateriaV, that.minBateriaV) &&
            Objects.equals(totalLecturas, that.totalLecturas) &&
            Objects.equals(calculadoEn, that.calculadoEn) &&
            Objects.equals(inversorId, that.inversorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fecha,
            energiaPvWh,
            energiaCargaWh,
            energiaRedWh,
            eficienciaPromPct,
            horasSol,
            picoPvW,
            picoCargaW,
            maxBateriaV,
            minBateriaV,
            totalLecturas,
            calculadoEn,
            inversorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoEficienciaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalEnergiaPvWh().map(f -> "energiaPvWh=" + f + ", ").orElse("") +
            optionalEnergiaCargaWh().map(f -> "energiaCargaWh=" + f + ", ").orElse("") +
            optionalEnergiaRedWh().map(f -> "energiaRedWh=" + f + ", ").orElse("") +
            optionalEficienciaPromPct().map(f -> "eficienciaPromPct=" + f + ", ").orElse("") +
            optionalHorasSol().map(f -> "horasSol=" + f + ", ").orElse("") +
            optionalPicoPvW().map(f -> "picoPvW=" + f + ", ").orElse("") +
            optionalPicoCargaW().map(f -> "picoCargaW=" + f + ", ").orElse("") +
            optionalMaxBateriaV().map(f -> "maxBateriaV=" + f + ", ").orElse("") +
            optionalMinBateriaV().map(f -> "minBateriaV=" + f + ", ").orElse("") +
            optionalTotalLecturas().map(f -> "totalLecturas=" + f + ", ").orElse("") +
            optionalCalculadoEn().map(f -> "calculadoEn=" + f + ", ").orElse("") +
            optionalInversorId().map(f -> "inversorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

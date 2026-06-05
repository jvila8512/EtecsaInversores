package com.etecsa.monitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.DatoInversor} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.DatoInversorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dato-inversors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoInversorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter tiempo;

    private DoubleFilter bateriaVoltajeV;

    private DoubleFilter bateriaCorrienteA;

    private DoubleFilter bateriaPotenciaW;

    private DoubleFilter pvVoltajeV;

    private DoubleFilter pvPotenciaW;

    private DoubleFilter salidaVoltajeV;

    private DoubleFilter salidaPotenciaW;

    private DoubleFilter salidaAparenteVa;

    private IntegerFilter cargaPorcentaje;

    private DoubleFilter redVoltajeV;

    private DoubleFilter redFrecuenciaHz;

    private StringFilter modoOperacion;

    private IntegerFilter codigoFallo;

    private DoubleFilter eficienciaPct;

    private DoubleFilter consumoRedW;

    private BooleanFilter bateriaCargando;

    private BooleanFilter pvActivo;

    private BooleanFilter redActiva;

    private BooleanFilter crcValido;

    private IntegerFilter latenciaMs;

    private LongFilter inversorId;

    private Boolean distinct;

    public DatoInversorCriteria() {}

    public DatoInversorCriteria(DatoInversorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tiempo = other.optionalTiempo().map(InstantFilter::copy).orElse(null);
        this.bateriaVoltajeV = other.optionalBateriaVoltajeV().map(DoubleFilter::copy).orElse(null);
        this.bateriaCorrienteA = other.optionalBateriaCorrienteA().map(DoubleFilter::copy).orElse(null);
        this.bateriaPotenciaW = other.optionalBateriaPotenciaW().map(DoubleFilter::copy).orElse(null);
        this.pvVoltajeV = other.optionalPvVoltajeV().map(DoubleFilter::copy).orElse(null);
        this.pvPotenciaW = other.optionalPvPotenciaW().map(DoubleFilter::copy).orElse(null);
        this.salidaVoltajeV = other.optionalSalidaVoltajeV().map(DoubleFilter::copy).orElse(null);
        this.salidaPotenciaW = other.optionalSalidaPotenciaW().map(DoubleFilter::copy).orElse(null);
        this.salidaAparenteVa = other.optionalSalidaAparenteVa().map(DoubleFilter::copy).orElse(null);
        this.cargaPorcentaje = other.optionalCargaPorcentaje().map(IntegerFilter::copy).orElse(null);
        this.redVoltajeV = other.optionalRedVoltajeV().map(DoubleFilter::copy).orElse(null);
        this.redFrecuenciaHz = other.optionalRedFrecuenciaHz().map(DoubleFilter::copy).orElse(null);
        this.modoOperacion = other.optionalModoOperacion().map(StringFilter::copy).orElse(null);
        this.codigoFallo = other.optionalCodigoFallo().map(IntegerFilter::copy).orElse(null);
        this.eficienciaPct = other.optionalEficienciaPct().map(DoubleFilter::copy).orElse(null);
        this.consumoRedW = other.optionalConsumoRedW().map(DoubleFilter::copy).orElse(null);
        this.bateriaCargando = other.optionalBateriaCargando().map(BooleanFilter::copy).orElse(null);
        this.pvActivo = other.optionalPvActivo().map(BooleanFilter::copy).orElse(null);
        this.redActiva = other.optionalRedActiva().map(BooleanFilter::copy).orElse(null);
        this.crcValido = other.optionalCrcValido().map(BooleanFilter::copy).orElse(null);
        this.latenciaMs = other.optionalLatenciaMs().map(IntegerFilter::copy).orElse(null);
        this.inversorId = other.optionalInversorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DatoInversorCriteria copy() {
        return new DatoInversorCriteria(this);
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

    public InstantFilter getTiempo() {
        return tiempo;
    }

    public Optional<InstantFilter> optionalTiempo() {
        return Optional.ofNullable(tiempo);
    }

    public InstantFilter tiempo() {
        if (tiempo == null) {
            setTiempo(new InstantFilter());
        }
        return tiempo;
    }

    public void setTiempo(InstantFilter tiempo) {
        this.tiempo = tiempo;
    }

    public DoubleFilter getBateriaVoltajeV() {
        return bateriaVoltajeV;
    }

    public Optional<DoubleFilter> optionalBateriaVoltajeV() {
        return Optional.ofNullable(bateriaVoltajeV);
    }

    public DoubleFilter bateriaVoltajeV() {
        if (bateriaVoltajeV == null) {
            setBateriaVoltajeV(new DoubleFilter());
        }
        return bateriaVoltajeV;
    }

    public void setBateriaVoltajeV(DoubleFilter bateriaVoltajeV) {
        this.bateriaVoltajeV = bateriaVoltajeV;
    }

    public DoubleFilter getBateriaCorrienteA() {
        return bateriaCorrienteA;
    }

    public Optional<DoubleFilter> optionalBateriaCorrienteA() {
        return Optional.ofNullable(bateriaCorrienteA);
    }

    public DoubleFilter bateriaCorrienteA() {
        if (bateriaCorrienteA == null) {
            setBateriaCorrienteA(new DoubleFilter());
        }
        return bateriaCorrienteA;
    }

    public void setBateriaCorrienteA(DoubleFilter bateriaCorrienteA) {
        this.bateriaCorrienteA = bateriaCorrienteA;
    }

    public DoubleFilter getBateriaPotenciaW() {
        return bateriaPotenciaW;
    }

    public Optional<DoubleFilter> optionalBateriaPotenciaW() {
        return Optional.ofNullable(bateriaPotenciaW);
    }

    public DoubleFilter bateriaPotenciaW() {
        if (bateriaPotenciaW == null) {
            setBateriaPotenciaW(new DoubleFilter());
        }
        return bateriaPotenciaW;
    }

    public void setBateriaPotenciaW(DoubleFilter bateriaPotenciaW) {
        this.bateriaPotenciaW = bateriaPotenciaW;
    }

    public DoubleFilter getPvVoltajeV() {
        return pvVoltajeV;
    }

    public Optional<DoubleFilter> optionalPvVoltajeV() {
        return Optional.ofNullable(pvVoltajeV);
    }

    public DoubleFilter pvVoltajeV() {
        if (pvVoltajeV == null) {
            setPvVoltajeV(new DoubleFilter());
        }
        return pvVoltajeV;
    }

    public void setPvVoltajeV(DoubleFilter pvVoltajeV) {
        this.pvVoltajeV = pvVoltajeV;
    }

    public DoubleFilter getPvPotenciaW() {
        return pvPotenciaW;
    }

    public Optional<DoubleFilter> optionalPvPotenciaW() {
        return Optional.ofNullable(pvPotenciaW);
    }

    public DoubleFilter pvPotenciaW() {
        if (pvPotenciaW == null) {
            setPvPotenciaW(new DoubleFilter());
        }
        return pvPotenciaW;
    }

    public void setPvPotenciaW(DoubleFilter pvPotenciaW) {
        this.pvPotenciaW = pvPotenciaW;
    }

    public DoubleFilter getSalidaVoltajeV() {
        return salidaVoltajeV;
    }

    public Optional<DoubleFilter> optionalSalidaVoltajeV() {
        return Optional.ofNullable(salidaVoltajeV);
    }

    public DoubleFilter salidaVoltajeV() {
        if (salidaVoltajeV == null) {
            setSalidaVoltajeV(new DoubleFilter());
        }
        return salidaVoltajeV;
    }

    public void setSalidaVoltajeV(DoubleFilter salidaVoltajeV) {
        this.salidaVoltajeV = salidaVoltajeV;
    }

    public DoubleFilter getSalidaPotenciaW() {
        return salidaPotenciaW;
    }

    public Optional<DoubleFilter> optionalSalidaPotenciaW() {
        return Optional.ofNullable(salidaPotenciaW);
    }

    public DoubleFilter salidaPotenciaW() {
        if (salidaPotenciaW == null) {
            setSalidaPotenciaW(new DoubleFilter());
        }
        return salidaPotenciaW;
    }

    public void setSalidaPotenciaW(DoubleFilter salidaPotenciaW) {
        this.salidaPotenciaW = salidaPotenciaW;
    }

    public DoubleFilter getSalidaAparenteVa() {
        return salidaAparenteVa;
    }

    public Optional<DoubleFilter> optionalSalidaAparenteVa() {
        return Optional.ofNullable(salidaAparenteVa);
    }

    public DoubleFilter salidaAparenteVa() {
        if (salidaAparenteVa == null) {
            setSalidaAparenteVa(new DoubleFilter());
        }
        return salidaAparenteVa;
    }

    public void setSalidaAparenteVa(DoubleFilter salidaAparenteVa) {
        this.salidaAparenteVa = salidaAparenteVa;
    }

    public IntegerFilter getCargaPorcentaje() {
        return cargaPorcentaje;
    }

    public Optional<IntegerFilter> optionalCargaPorcentaje() {
        return Optional.ofNullable(cargaPorcentaje);
    }

    public IntegerFilter cargaPorcentaje() {
        if (cargaPorcentaje == null) {
            setCargaPorcentaje(new IntegerFilter());
        }
        return cargaPorcentaje;
    }

    public void setCargaPorcentaje(IntegerFilter cargaPorcentaje) {
        this.cargaPorcentaje = cargaPorcentaje;
    }

    public DoubleFilter getRedVoltajeV() {
        return redVoltajeV;
    }

    public Optional<DoubleFilter> optionalRedVoltajeV() {
        return Optional.ofNullable(redVoltajeV);
    }

    public DoubleFilter redVoltajeV() {
        if (redVoltajeV == null) {
            setRedVoltajeV(new DoubleFilter());
        }
        return redVoltajeV;
    }

    public void setRedVoltajeV(DoubleFilter redVoltajeV) {
        this.redVoltajeV = redVoltajeV;
    }

    public DoubleFilter getRedFrecuenciaHz() {
        return redFrecuenciaHz;
    }

    public Optional<DoubleFilter> optionalRedFrecuenciaHz() {
        return Optional.ofNullable(redFrecuenciaHz);
    }

    public DoubleFilter redFrecuenciaHz() {
        if (redFrecuenciaHz == null) {
            setRedFrecuenciaHz(new DoubleFilter());
        }
        return redFrecuenciaHz;
    }

    public void setRedFrecuenciaHz(DoubleFilter redFrecuenciaHz) {
        this.redFrecuenciaHz = redFrecuenciaHz;
    }

    public StringFilter getModoOperacion() {
        return modoOperacion;
    }

    public Optional<StringFilter> optionalModoOperacion() {
        return Optional.ofNullable(modoOperacion);
    }

    public StringFilter modoOperacion() {
        if (modoOperacion == null) {
            setModoOperacion(new StringFilter());
        }
        return modoOperacion;
    }

    public void setModoOperacion(StringFilter modoOperacion) {
        this.modoOperacion = modoOperacion;
    }

    public IntegerFilter getCodigoFallo() {
        return codigoFallo;
    }

    public Optional<IntegerFilter> optionalCodigoFallo() {
        return Optional.ofNullable(codigoFallo);
    }

    public IntegerFilter codigoFallo() {
        if (codigoFallo == null) {
            setCodigoFallo(new IntegerFilter());
        }
        return codigoFallo;
    }

    public void setCodigoFallo(IntegerFilter codigoFallo) {
        this.codigoFallo = codigoFallo;
    }

    public DoubleFilter getEficienciaPct() {
        return eficienciaPct;
    }

    public Optional<DoubleFilter> optionalEficienciaPct() {
        return Optional.ofNullable(eficienciaPct);
    }

    public DoubleFilter eficienciaPct() {
        if (eficienciaPct == null) {
            setEficienciaPct(new DoubleFilter());
        }
        return eficienciaPct;
    }

    public void setEficienciaPct(DoubleFilter eficienciaPct) {
        this.eficienciaPct = eficienciaPct;
    }

    public DoubleFilter getConsumoRedW() {
        return consumoRedW;
    }

    public Optional<DoubleFilter> optionalConsumoRedW() {
        return Optional.ofNullable(consumoRedW);
    }

    public DoubleFilter consumoRedW() {
        if (consumoRedW == null) {
            setConsumoRedW(new DoubleFilter());
        }
        return consumoRedW;
    }

    public void setConsumoRedW(DoubleFilter consumoRedW) {
        this.consumoRedW = consumoRedW;
    }

    public BooleanFilter getBateriaCargando() {
        return bateriaCargando;
    }

    public Optional<BooleanFilter> optionalBateriaCargando() {
        return Optional.ofNullable(bateriaCargando);
    }

    public BooleanFilter bateriaCargando() {
        if (bateriaCargando == null) {
            setBateriaCargando(new BooleanFilter());
        }
        return bateriaCargando;
    }

    public void setBateriaCargando(BooleanFilter bateriaCargando) {
        this.bateriaCargando = bateriaCargando;
    }

    public BooleanFilter getPvActivo() {
        return pvActivo;
    }

    public Optional<BooleanFilter> optionalPvActivo() {
        return Optional.ofNullable(pvActivo);
    }

    public BooleanFilter pvActivo() {
        if (pvActivo == null) {
            setPvActivo(new BooleanFilter());
        }
        return pvActivo;
    }

    public void setPvActivo(BooleanFilter pvActivo) {
        this.pvActivo = pvActivo;
    }

    public BooleanFilter getRedActiva() {
        return redActiva;
    }

    public Optional<BooleanFilter> optionalRedActiva() {
        return Optional.ofNullable(redActiva);
    }

    public BooleanFilter redActiva() {
        if (redActiva == null) {
            setRedActiva(new BooleanFilter());
        }
        return redActiva;
    }

    public void setRedActiva(BooleanFilter redActiva) {
        this.redActiva = redActiva;
    }

    public BooleanFilter getCrcValido() {
        return crcValido;
    }

    public Optional<BooleanFilter> optionalCrcValido() {
        return Optional.ofNullable(crcValido);
    }

    public BooleanFilter crcValido() {
        if (crcValido == null) {
            setCrcValido(new BooleanFilter());
        }
        return crcValido;
    }

    public void setCrcValido(BooleanFilter crcValido) {
        this.crcValido = crcValido;
    }

    public IntegerFilter getLatenciaMs() {
        return latenciaMs;
    }

    public Optional<IntegerFilter> optionalLatenciaMs() {
        return Optional.ofNullable(latenciaMs);
    }

    public IntegerFilter latenciaMs() {
        if (latenciaMs == null) {
            setLatenciaMs(new IntegerFilter());
        }
        return latenciaMs;
    }

    public void setLatenciaMs(IntegerFilter latenciaMs) {
        this.latenciaMs = latenciaMs;
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
        final DatoInversorCriteria that = (DatoInversorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tiempo, that.tiempo) &&
            Objects.equals(bateriaVoltajeV, that.bateriaVoltajeV) &&
            Objects.equals(bateriaCorrienteA, that.bateriaCorrienteA) &&
            Objects.equals(bateriaPotenciaW, that.bateriaPotenciaW) &&
            Objects.equals(pvVoltajeV, that.pvVoltajeV) &&
            Objects.equals(pvPotenciaW, that.pvPotenciaW) &&
            Objects.equals(salidaVoltajeV, that.salidaVoltajeV) &&
            Objects.equals(salidaPotenciaW, that.salidaPotenciaW) &&
            Objects.equals(salidaAparenteVa, that.salidaAparenteVa) &&
            Objects.equals(cargaPorcentaje, that.cargaPorcentaje) &&
            Objects.equals(redVoltajeV, that.redVoltajeV) &&
            Objects.equals(redFrecuenciaHz, that.redFrecuenciaHz) &&
            Objects.equals(modoOperacion, that.modoOperacion) &&
            Objects.equals(codigoFallo, that.codigoFallo) &&
            Objects.equals(eficienciaPct, that.eficienciaPct) &&
            Objects.equals(consumoRedW, that.consumoRedW) &&
            Objects.equals(bateriaCargando, that.bateriaCargando) &&
            Objects.equals(pvActivo, that.pvActivo) &&
            Objects.equals(redActiva, that.redActiva) &&
            Objects.equals(crcValido, that.crcValido) &&
            Objects.equals(latenciaMs, that.latenciaMs) &&
            Objects.equals(inversorId, that.inversorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tiempo,
            bateriaVoltajeV,
            bateriaCorrienteA,
            bateriaPotenciaW,
            pvVoltajeV,
            pvPotenciaW,
            salidaVoltajeV,
            salidaPotenciaW,
            salidaAparenteVa,
            cargaPorcentaje,
            redVoltajeV,
            redFrecuenciaHz,
            modoOperacion,
            codigoFallo,
            eficienciaPct,
            consumoRedW,
            bateriaCargando,
            pvActivo,
            redActiva,
            crcValido,
            latenciaMs,
            inversorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoInversorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTiempo().map(f -> "tiempo=" + f + ", ").orElse("") +
            optionalBateriaVoltajeV().map(f -> "bateriaVoltajeV=" + f + ", ").orElse("") +
            optionalBateriaCorrienteA().map(f -> "bateriaCorrienteA=" + f + ", ").orElse("") +
            optionalBateriaPotenciaW().map(f -> "bateriaPotenciaW=" + f + ", ").orElse("") +
            optionalPvVoltajeV().map(f -> "pvVoltajeV=" + f + ", ").orElse("") +
            optionalPvPotenciaW().map(f -> "pvPotenciaW=" + f + ", ").orElse("") +
            optionalSalidaVoltajeV().map(f -> "salidaVoltajeV=" + f + ", ").orElse("") +
            optionalSalidaPotenciaW().map(f -> "salidaPotenciaW=" + f + ", ").orElse("") +
            optionalSalidaAparenteVa().map(f -> "salidaAparenteVa=" + f + ", ").orElse("") +
            optionalCargaPorcentaje().map(f -> "cargaPorcentaje=" + f + ", ").orElse("") +
            optionalRedVoltajeV().map(f -> "redVoltajeV=" + f + ", ").orElse("") +
            optionalRedFrecuenciaHz().map(f -> "redFrecuenciaHz=" + f + ", ").orElse("") +
            optionalModoOperacion().map(f -> "modoOperacion=" + f + ", ").orElse("") +
            optionalCodigoFallo().map(f -> "codigoFallo=" + f + ", ").orElse("") +
            optionalEficienciaPct().map(f -> "eficienciaPct=" + f + ", ").orElse("") +
            optionalConsumoRedW().map(f -> "consumoRedW=" + f + ", ").orElse("") +
            optionalBateriaCargando().map(f -> "bateriaCargando=" + f + ", ").orElse("") +
            optionalPvActivo().map(f -> "pvActivo=" + f + ", ").orElse("") +
            optionalRedActiva().map(f -> "redActiva=" + f + ", ").orElse("") +
            optionalCrcValido().map(f -> "crcValido=" + f + ", ").orElse("") +
            optionalLatenciaMs().map(f -> "latenciaMs=" + f + ", ").orElse("") +
            optionalInversorId().map(f -> "inversorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

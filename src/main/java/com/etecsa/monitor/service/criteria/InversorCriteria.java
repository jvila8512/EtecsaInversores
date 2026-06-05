package com.etecsa.monitor.service.criteria;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.domain.enumeration.TipoConexion;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.Inversor} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.InversorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inversors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InversorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoConexion
     */
    public static class TipoConexionFilter extends Filter<TipoConexion> {

        public TipoConexionFilter() {}

        public TipoConexionFilter(TipoConexionFilter filter) {
            super(filter);
        }

        @Override
        public TipoConexionFilter copy() {
            return new TipoConexionFilter(this);
        }
    }

    /**
     * Class for filtering EstadoDispositivo
     */
    public static class EstadoDispositivoFilter extends Filter<EstadoDispositivo> {

        public EstadoDispositivoFilter() {}

        public EstadoDispositivoFilter(EstadoDispositivoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoDispositivoFilter copy() {
            return new EstadoDispositivoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter numeroSerie;

    private BooleanFilter esMaster;

    private TipoConexionFilter tipoConexion;

    private StringFilter puertoSerial;

    private IntegerFilter slaveId;

    private BooleanFilter activo;

    private EstadoDispositivoFilter estadoDispositivo;

    private InstantFilter ultimaLectura;

    private StringFilter ultimoError;

    private IntegerFilter periodoMuestreoSeg;

    private InstantFilter fechaRegistro;

    private InstantFilter ultimaConexion;

    private LongFilter sitioId;

    private LongFilter marcaId;

    private LongFilter modeloId;

    private LongFilter gatewayId;

    private LongFilter masterReferenciaId;

    private Boolean distinct;

    public InversorCriteria() {}

    public InversorCriteria(InversorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.numeroSerie = other.optionalNumeroSerie().map(StringFilter::copy).orElse(null);
        this.esMaster = other.optionalEsMaster().map(BooleanFilter::copy).orElse(null);
        this.tipoConexion = other.optionalTipoConexion().map(TipoConexionFilter::copy).orElse(null);
        this.puertoSerial = other.optionalPuertoSerial().map(StringFilter::copy).orElse(null);
        this.slaveId = other.optionalSlaveId().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.estadoDispositivo = other.optionalEstadoDispositivo().map(EstadoDispositivoFilter::copy).orElse(null);
        this.ultimaLectura = other.optionalUltimaLectura().map(InstantFilter::copy).orElse(null);
        this.ultimoError = other.optionalUltimoError().map(StringFilter::copy).orElse(null);
        this.periodoMuestreoSeg = other.optionalPeriodoMuestreoSeg().map(IntegerFilter::copy).orElse(null);
        this.fechaRegistro = other.optionalFechaRegistro().map(InstantFilter::copy).orElse(null);
        this.ultimaConexion = other.optionalUltimaConexion().map(InstantFilter::copy).orElse(null);
        this.sitioId = other.optionalSitioId().map(LongFilter::copy).orElse(null);
        this.marcaId = other.optionalMarcaId().map(LongFilter::copy).orElse(null);
        this.modeloId = other.optionalModeloId().map(LongFilter::copy).orElse(null);
        this.gatewayId = other.optionalGatewayId().map(LongFilter::copy).orElse(null);
        this.masterReferenciaId = other.optionalMasterReferenciaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InversorCriteria copy() {
        return new InversorCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getNumeroSerie() {
        return numeroSerie;
    }

    public Optional<StringFilter> optionalNumeroSerie() {
        return Optional.ofNullable(numeroSerie);
    }

    public StringFilter numeroSerie() {
        if (numeroSerie == null) {
            setNumeroSerie(new StringFilter());
        }
        return numeroSerie;
    }

    public void setNumeroSerie(StringFilter numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public BooleanFilter getEsMaster() {
        return esMaster;
    }

    public Optional<BooleanFilter> optionalEsMaster() {
        return Optional.ofNullable(esMaster);
    }

    public BooleanFilter esMaster() {
        if (esMaster == null) {
            setEsMaster(new BooleanFilter());
        }
        return esMaster;
    }

    public void setEsMaster(BooleanFilter esMaster) {
        this.esMaster = esMaster;
    }

    public TipoConexionFilter getTipoConexion() {
        return tipoConexion;
    }

    public Optional<TipoConexionFilter> optionalTipoConexion() {
        return Optional.ofNullable(tipoConexion);
    }

    public TipoConexionFilter tipoConexion() {
        if (tipoConexion == null) {
            setTipoConexion(new TipoConexionFilter());
        }
        return tipoConexion;
    }

    public void setTipoConexion(TipoConexionFilter tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public StringFilter getPuertoSerial() {
        return puertoSerial;
    }

    public Optional<StringFilter> optionalPuertoSerial() {
        return Optional.ofNullable(puertoSerial);
    }

    public StringFilter puertoSerial() {
        if (puertoSerial == null) {
            setPuertoSerial(new StringFilter());
        }
        return puertoSerial;
    }

    public void setPuertoSerial(StringFilter puertoSerial) {
        this.puertoSerial = puertoSerial;
    }

    public IntegerFilter getSlaveId() {
        return slaveId;
    }

    public Optional<IntegerFilter> optionalSlaveId() {
        return Optional.ofNullable(slaveId);
    }

    public IntegerFilter slaveId() {
        if (slaveId == null) {
            setSlaveId(new IntegerFilter());
        }
        return slaveId;
    }

    public void setSlaveId(IntegerFilter slaveId) {
        this.slaveId = slaveId;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
    }

    public EstadoDispositivoFilter getEstadoDispositivo() {
        return estadoDispositivo;
    }

    public Optional<EstadoDispositivoFilter> optionalEstadoDispositivo() {
        return Optional.ofNullable(estadoDispositivo);
    }

    public EstadoDispositivoFilter estadoDispositivo() {
        if (estadoDispositivo == null) {
            setEstadoDispositivo(new EstadoDispositivoFilter());
        }
        return estadoDispositivo;
    }

    public void setEstadoDispositivo(EstadoDispositivoFilter estadoDispositivo) {
        this.estadoDispositivo = estadoDispositivo;
    }

    public InstantFilter getUltimaLectura() {
        return ultimaLectura;
    }

    public Optional<InstantFilter> optionalUltimaLectura() {
        return Optional.ofNullable(ultimaLectura);
    }

    public InstantFilter ultimaLectura() {
        if (ultimaLectura == null) {
            setUltimaLectura(new InstantFilter());
        }
        return ultimaLectura;
    }

    public void setUltimaLectura(InstantFilter ultimaLectura) {
        this.ultimaLectura = ultimaLectura;
    }

    public StringFilter getUltimoError() {
        return ultimoError;
    }

    public Optional<StringFilter> optionalUltimoError() {
        return Optional.ofNullable(ultimoError);
    }

    public StringFilter ultimoError() {
        if (ultimoError == null) {
            setUltimoError(new StringFilter());
        }
        return ultimoError;
    }

    public void setUltimoError(StringFilter ultimoError) {
        this.ultimoError = ultimoError;
    }

    public IntegerFilter getPeriodoMuestreoSeg() {
        return periodoMuestreoSeg;
    }

    public Optional<IntegerFilter> optionalPeriodoMuestreoSeg() {
        return Optional.ofNullable(periodoMuestreoSeg);
    }

    public IntegerFilter periodoMuestreoSeg() {
        if (periodoMuestreoSeg == null) {
            setPeriodoMuestreoSeg(new IntegerFilter());
        }
        return periodoMuestreoSeg;
    }

    public void setPeriodoMuestreoSeg(IntegerFilter periodoMuestreoSeg) {
        this.periodoMuestreoSeg = periodoMuestreoSeg;
    }

    public InstantFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public Optional<InstantFilter> optionalFechaRegistro() {
        return Optional.ofNullable(fechaRegistro);
    }

    public InstantFilter fechaRegistro() {
        if (fechaRegistro == null) {
            setFechaRegistro(new InstantFilter());
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(InstantFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public InstantFilter getUltimaConexion() {
        return ultimaConexion;
    }

    public Optional<InstantFilter> optionalUltimaConexion() {
        return Optional.ofNullable(ultimaConexion);
    }

    public InstantFilter ultimaConexion() {
        if (ultimaConexion == null) {
            setUltimaConexion(new InstantFilter());
        }
        return ultimaConexion;
    }

    public void setUltimaConexion(InstantFilter ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public LongFilter getSitioId() {
        return sitioId;
    }

    public Optional<LongFilter> optionalSitioId() {
        return Optional.ofNullable(sitioId);
    }

    public LongFilter sitioId() {
        if (sitioId == null) {
            setSitioId(new LongFilter());
        }
        return sitioId;
    }

    public void setSitioId(LongFilter sitioId) {
        this.sitioId = sitioId;
    }

    public LongFilter getMarcaId() {
        return marcaId;
    }

    public Optional<LongFilter> optionalMarcaId() {
        return Optional.ofNullable(marcaId);
    }

    public LongFilter marcaId() {
        if (marcaId == null) {
            setMarcaId(new LongFilter());
        }
        return marcaId;
    }

    public void setMarcaId(LongFilter marcaId) {
        this.marcaId = marcaId;
    }

    public LongFilter getModeloId() {
        return modeloId;
    }

    public Optional<LongFilter> optionalModeloId() {
        return Optional.ofNullable(modeloId);
    }

    public LongFilter modeloId() {
        if (modeloId == null) {
            setModeloId(new LongFilter());
        }
        return modeloId;
    }

    public void setModeloId(LongFilter modeloId) {
        this.modeloId = modeloId;
    }

    public LongFilter getGatewayId() {
        return gatewayId;
    }

    public Optional<LongFilter> optionalGatewayId() {
        return Optional.ofNullable(gatewayId);
    }

    public LongFilter gatewayId() {
        if (gatewayId == null) {
            setGatewayId(new LongFilter());
        }
        return gatewayId;
    }

    public void setGatewayId(LongFilter gatewayId) {
        this.gatewayId = gatewayId;
    }

    public LongFilter getMasterReferenciaId() {
        return masterReferenciaId;
    }

    public Optional<LongFilter> optionalMasterReferenciaId() {
        return Optional.ofNullable(masterReferenciaId);
    }

    public LongFilter masterReferenciaId() {
        if (masterReferenciaId == null) {
            setMasterReferenciaId(new LongFilter());
        }
        return masterReferenciaId;
    }

    public void setMasterReferenciaId(LongFilter masterReferenciaId) {
        this.masterReferenciaId = masterReferenciaId;
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
        final InversorCriteria that = (InversorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(numeroSerie, that.numeroSerie) &&
            Objects.equals(esMaster, that.esMaster) &&
            Objects.equals(tipoConexion, that.tipoConexion) &&
            Objects.equals(puertoSerial, that.puertoSerial) &&
            Objects.equals(slaveId, that.slaveId) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(estadoDispositivo, that.estadoDispositivo) &&
            Objects.equals(ultimaLectura, that.ultimaLectura) &&
            Objects.equals(ultimoError, that.ultimoError) &&
            Objects.equals(periodoMuestreoSeg, that.periodoMuestreoSeg) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(ultimaConexion, that.ultimaConexion) &&
            Objects.equals(sitioId, that.sitioId) &&
            Objects.equals(marcaId, that.marcaId) &&
            Objects.equals(modeloId, that.modeloId) &&
            Objects.equals(gatewayId, that.gatewayId) &&
            Objects.equals(masterReferenciaId, that.masterReferenciaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            numeroSerie,
            esMaster,
            tipoConexion,
            puertoSerial,
            slaveId,
            activo,
            estadoDispositivo,
            ultimaLectura,
            ultimoError,
            periodoMuestreoSeg,
            fechaRegistro,
            ultimaConexion,
            sitioId,
            marcaId,
            modeloId,
            gatewayId,
            masterReferenciaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InversorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalNumeroSerie().map(f -> "numeroSerie=" + f + ", ").orElse("") +
            optionalEsMaster().map(f -> "esMaster=" + f + ", ").orElse("") +
            optionalTipoConexion().map(f -> "tipoConexion=" + f + ", ").orElse("") +
            optionalPuertoSerial().map(f -> "puertoSerial=" + f + ", ").orElse("") +
            optionalSlaveId().map(f -> "slaveId=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalEstadoDispositivo().map(f -> "estadoDispositivo=" + f + ", ").orElse("") +
            optionalUltimaLectura().map(f -> "ultimaLectura=" + f + ", ").orElse("") +
            optionalUltimoError().map(f -> "ultimoError=" + f + ", ").orElse("") +
            optionalPeriodoMuestreoSeg().map(f -> "periodoMuestreoSeg=" + f + ", ").orElse("") +
            optionalFechaRegistro().map(f -> "fechaRegistro=" + f + ", ").orElse("") +
            optionalUltimaConexion().map(f -> "ultimaConexion=" + f + ", ").orElse("") +
            optionalSitioId().map(f -> "sitioId=" + f + ", ").orElse("") +
            optionalMarcaId().map(f -> "marcaId=" + f + ", ").orElse("") +
            optionalModeloId().map(f -> "modeloId=" + f + ", ").orElse("") +
            optionalGatewayId().map(f -> "gatewayId=" + f + ", ").orElse("") +
            optionalMasterReferenciaId().map(f -> "masterReferenciaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

package com.etecsa.monitor.service.criteria;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.GatewayTcp} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.GatewayTcpResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gateway-tcps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GatewayTcpCriteria implements Serializable, Criteria {

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

    private StringFilter descripcion;

    private StringFilter ubicacion;

    private StringFilter direccionIp;

    private IntegerFilter puerto;

    private BooleanFilter activo;

    private EstadoDispositivoFilter estadoConexion;

    private InstantFilter ultimaLectura;

    private StringFilter ultimoError;

    private IntegerFilter tiempoEsperaMs;

    private IntegerFilter reintentos;

    private LongFilter sitioId;

    private Boolean distinct;

    public GatewayTcpCriteria() {}

    public GatewayTcpCriteria(GatewayTcpCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.ubicacion = other.optionalUbicacion().map(StringFilter::copy).orElse(null);
        this.direccionIp = other.optionalDireccionIp().map(StringFilter::copy).orElse(null);
        this.puerto = other.optionalPuerto().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.estadoConexion = other.optionalEstadoConexion().map(EstadoDispositivoFilter::copy).orElse(null);
        this.ultimaLectura = other.optionalUltimaLectura().map(InstantFilter::copy).orElse(null);
        this.ultimoError = other.optionalUltimoError().map(StringFilter::copy).orElse(null);
        this.tiempoEsperaMs = other.optionalTiempoEsperaMs().map(IntegerFilter::copy).orElse(null);
        this.reintentos = other.optionalReintentos().map(IntegerFilter::copy).orElse(null);
        this.sitioId = other.optionalSitioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GatewayTcpCriteria copy() {
        return new GatewayTcpCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getUbicacion() {
        return ubicacion;
    }

    public Optional<StringFilter> optionalUbicacion() {
        return Optional.ofNullable(ubicacion);
    }

    public StringFilter ubicacion() {
        if (ubicacion == null) {
            setUbicacion(new StringFilter());
        }
        return ubicacion;
    }

    public void setUbicacion(StringFilter ubicacion) {
        this.ubicacion = ubicacion;
    }

    public StringFilter getDireccionIp() {
        return direccionIp;
    }

    public Optional<StringFilter> optionalDireccionIp() {
        return Optional.ofNullable(direccionIp);
    }

    public StringFilter direccionIp() {
        if (direccionIp == null) {
            setDireccionIp(new StringFilter());
        }
        return direccionIp;
    }

    public void setDireccionIp(StringFilter direccionIp) {
        this.direccionIp = direccionIp;
    }

    public IntegerFilter getPuerto() {
        return puerto;
    }

    public Optional<IntegerFilter> optionalPuerto() {
        return Optional.ofNullable(puerto);
    }

    public IntegerFilter puerto() {
        if (puerto == null) {
            setPuerto(new IntegerFilter());
        }
        return puerto;
    }

    public void setPuerto(IntegerFilter puerto) {
        this.puerto = puerto;
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

    public EstadoDispositivoFilter getEstadoConexion() {
        return estadoConexion;
    }

    public Optional<EstadoDispositivoFilter> optionalEstadoConexion() {
        return Optional.ofNullable(estadoConexion);
    }

    public EstadoDispositivoFilter estadoConexion() {
        if (estadoConexion == null) {
            setEstadoConexion(new EstadoDispositivoFilter());
        }
        return estadoConexion;
    }

    public void setEstadoConexion(EstadoDispositivoFilter estadoConexion) {
        this.estadoConexion = estadoConexion;
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

    public IntegerFilter getTiempoEsperaMs() {
        return tiempoEsperaMs;
    }

    public Optional<IntegerFilter> optionalTiempoEsperaMs() {
        return Optional.ofNullable(tiempoEsperaMs);
    }

    public IntegerFilter tiempoEsperaMs() {
        if (tiempoEsperaMs == null) {
            setTiempoEsperaMs(new IntegerFilter());
        }
        return tiempoEsperaMs;
    }

    public void setTiempoEsperaMs(IntegerFilter tiempoEsperaMs) {
        this.tiempoEsperaMs = tiempoEsperaMs;
    }

    public IntegerFilter getReintentos() {
        return reintentos;
    }

    public Optional<IntegerFilter> optionalReintentos() {
        return Optional.ofNullable(reintentos);
    }

    public IntegerFilter reintentos() {
        if (reintentos == null) {
            setReintentos(new IntegerFilter());
        }
        return reintentos;
    }

    public void setReintentos(IntegerFilter reintentos) {
        this.reintentos = reintentos;
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
        final GatewayTcpCriteria that = (GatewayTcpCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(ubicacion, that.ubicacion) &&
            Objects.equals(direccionIp, that.direccionIp) &&
            Objects.equals(puerto, that.puerto) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(estadoConexion, that.estadoConexion) &&
            Objects.equals(ultimaLectura, that.ultimaLectura) &&
            Objects.equals(ultimoError, that.ultimoError) &&
            Objects.equals(tiempoEsperaMs, that.tiempoEsperaMs) &&
            Objects.equals(reintentos, that.reintentos) &&
            Objects.equals(sitioId, that.sitioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            ubicacion,
            direccionIp,
            puerto,
            activo,
            estadoConexion,
            ultimaLectura,
            ultimoError,
            tiempoEsperaMs,
            reintentos,
            sitioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GatewayTcpCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalUbicacion().map(f -> "ubicacion=" + f + ", ").orElse("") +
            optionalDireccionIp().map(f -> "direccionIp=" + f + ", ").orElse("") +
            optionalPuerto().map(f -> "puerto=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalEstadoConexion().map(f -> "estadoConexion=" + f + ", ").orElse("") +
            optionalUltimaLectura().map(f -> "ultimaLectura=" + f + ", ").orElse("") +
            optionalUltimoError().map(f -> "ultimoError=" + f + ", ").orElse("") +
            optionalTiempoEsperaMs().map(f -> "tiempoEsperaMs=" + f + ", ").orElse("") +
            optionalReintentos().map(f -> "reintentos=" + f + ", ").orElse("") +
            optionalSitioId().map(f -> "sitioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

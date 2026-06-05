package com.etecsa.monitor.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.EventoAlerta} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.EventoAlertaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evento-alertas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoAlertaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter timestamp;

    private StringFilter nombreAlerta;

    private StringFilter mensaje;

    private StringFilter campoMonitorizado;

    private DoubleFilter valorDetectado;

    private DoubleFilter umbral;

    private StringFilter operador;

    private StringFilter nivelSeveridad;

    private BooleanFilter notificacionEnviada;

    private StringFilter canalUsado;

    private BooleanFilter resuelto;

    private InstantFilter fechaResolucion;

    private StringFilter notas;

    private LongFilter inversorId;

    private LongFilter alertaConfigId;

    private Boolean distinct;

    public EventoAlertaCriteria() {}

    public EventoAlertaCriteria(EventoAlertaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.nombreAlerta = other.optionalNombreAlerta().map(StringFilter::copy).orElse(null);
        this.mensaje = other.optionalMensaje().map(StringFilter::copy).orElse(null);
        this.campoMonitorizado = other.optionalCampoMonitorizado().map(StringFilter::copy).orElse(null);
        this.valorDetectado = other.optionalValorDetectado().map(DoubleFilter::copy).orElse(null);
        this.umbral = other.optionalUmbral().map(DoubleFilter::copy).orElse(null);
        this.operador = other.optionalOperador().map(StringFilter::copy).orElse(null);
        this.nivelSeveridad = other.optionalNivelSeveridad().map(StringFilter::copy).orElse(null);
        this.notificacionEnviada = other.optionalNotificacionEnviada().map(BooleanFilter::copy).orElse(null);
        this.canalUsado = other.optionalCanalUsado().map(StringFilter::copy).orElse(null);
        this.resuelto = other.optionalResuelto().map(BooleanFilter::copy).orElse(null);
        this.fechaResolucion = other.optionalFechaResolucion().map(InstantFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.inversorId = other.optionalInversorId().map(LongFilter::copy).orElse(null);
        this.alertaConfigId = other.optionalAlertaConfigId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventoAlertaCriteria copy() {
        return new EventoAlertaCriteria(this);
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

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getNombreAlerta() {
        return nombreAlerta;
    }

    public Optional<StringFilter> optionalNombreAlerta() {
        return Optional.ofNullable(nombreAlerta);
    }

    public StringFilter nombreAlerta() {
        if (nombreAlerta == null) {
            setNombreAlerta(new StringFilter());
        }
        return nombreAlerta;
    }

    public void setNombreAlerta(StringFilter nombreAlerta) {
        this.nombreAlerta = nombreAlerta;
    }

    public StringFilter getMensaje() {
        return mensaje;
    }

    public Optional<StringFilter> optionalMensaje() {
        return Optional.ofNullable(mensaje);
    }

    public StringFilter mensaje() {
        if (mensaje == null) {
            setMensaje(new StringFilter());
        }
        return mensaje;
    }

    public void setMensaje(StringFilter mensaje) {
        this.mensaje = mensaje;
    }

    public StringFilter getCampoMonitorizado() {
        return campoMonitorizado;
    }

    public Optional<StringFilter> optionalCampoMonitorizado() {
        return Optional.ofNullable(campoMonitorizado);
    }

    public StringFilter campoMonitorizado() {
        if (campoMonitorizado == null) {
            setCampoMonitorizado(new StringFilter());
        }
        return campoMonitorizado;
    }

    public void setCampoMonitorizado(StringFilter campoMonitorizado) {
        this.campoMonitorizado = campoMonitorizado;
    }

    public DoubleFilter getValorDetectado() {
        return valorDetectado;
    }

    public Optional<DoubleFilter> optionalValorDetectado() {
        return Optional.ofNullable(valorDetectado);
    }

    public DoubleFilter valorDetectado() {
        if (valorDetectado == null) {
            setValorDetectado(new DoubleFilter());
        }
        return valorDetectado;
    }

    public void setValorDetectado(DoubleFilter valorDetectado) {
        this.valorDetectado = valorDetectado;
    }

    public DoubleFilter getUmbral() {
        return umbral;
    }

    public Optional<DoubleFilter> optionalUmbral() {
        return Optional.ofNullable(umbral);
    }

    public DoubleFilter umbral() {
        if (umbral == null) {
            setUmbral(new DoubleFilter());
        }
        return umbral;
    }

    public void setUmbral(DoubleFilter umbral) {
        this.umbral = umbral;
    }

    public StringFilter getOperador() {
        return operador;
    }

    public Optional<StringFilter> optionalOperador() {
        return Optional.ofNullable(operador);
    }

    public StringFilter operador() {
        if (operador == null) {
            setOperador(new StringFilter());
        }
        return operador;
    }

    public void setOperador(StringFilter operador) {
        this.operador = operador;
    }

    public StringFilter getNivelSeveridad() {
        return nivelSeveridad;
    }

    public Optional<StringFilter> optionalNivelSeveridad() {
        return Optional.ofNullable(nivelSeveridad);
    }

    public StringFilter nivelSeveridad() {
        if (nivelSeveridad == null) {
            setNivelSeveridad(new StringFilter());
        }
        return nivelSeveridad;
    }

    public void setNivelSeveridad(StringFilter nivelSeveridad) {
        this.nivelSeveridad = nivelSeveridad;
    }

    public BooleanFilter getNotificacionEnviada() {
        return notificacionEnviada;
    }

    public Optional<BooleanFilter> optionalNotificacionEnviada() {
        return Optional.ofNullable(notificacionEnviada);
    }

    public BooleanFilter notificacionEnviada() {
        if (notificacionEnviada == null) {
            setNotificacionEnviada(new BooleanFilter());
        }
        return notificacionEnviada;
    }

    public void setNotificacionEnviada(BooleanFilter notificacionEnviada) {
        this.notificacionEnviada = notificacionEnviada;
    }

    public StringFilter getCanalUsado() {
        return canalUsado;
    }

    public Optional<StringFilter> optionalCanalUsado() {
        return Optional.ofNullable(canalUsado);
    }

    public StringFilter canalUsado() {
        if (canalUsado == null) {
            setCanalUsado(new StringFilter());
        }
        return canalUsado;
    }

    public void setCanalUsado(StringFilter canalUsado) {
        this.canalUsado = canalUsado;
    }

    public BooleanFilter getResuelto() {
        return resuelto;
    }

    public Optional<BooleanFilter> optionalResuelto() {
        return Optional.ofNullable(resuelto);
    }

    public BooleanFilter resuelto() {
        if (resuelto == null) {
            setResuelto(new BooleanFilter());
        }
        return resuelto;
    }

    public void setResuelto(BooleanFilter resuelto) {
        this.resuelto = resuelto;
    }

    public InstantFilter getFechaResolucion() {
        return fechaResolucion;
    }

    public Optional<InstantFilter> optionalFechaResolucion() {
        return Optional.ofNullable(fechaResolucion);
    }

    public InstantFilter fechaResolucion() {
        if (fechaResolucion == null) {
            setFechaResolucion(new InstantFilter());
        }
        return fechaResolucion;
    }

    public void setFechaResolucion(InstantFilter fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public StringFilter getNotas() {
        return notas;
    }

    public Optional<StringFilter> optionalNotas() {
        return Optional.ofNullable(notas);
    }

    public StringFilter notas() {
        if (notas == null) {
            setNotas(new StringFilter());
        }
        return notas;
    }

    public void setNotas(StringFilter notas) {
        this.notas = notas;
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

    public LongFilter getAlertaConfigId() {
        return alertaConfigId;
    }

    public Optional<LongFilter> optionalAlertaConfigId() {
        return Optional.ofNullable(alertaConfigId);
    }

    public LongFilter alertaConfigId() {
        if (alertaConfigId == null) {
            setAlertaConfigId(new LongFilter());
        }
        return alertaConfigId;
    }

    public void setAlertaConfigId(LongFilter alertaConfigId) {
        this.alertaConfigId = alertaConfigId;
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
        final EventoAlertaCriteria that = (EventoAlertaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(nombreAlerta, that.nombreAlerta) &&
            Objects.equals(mensaje, that.mensaje) &&
            Objects.equals(campoMonitorizado, that.campoMonitorizado) &&
            Objects.equals(valorDetectado, that.valorDetectado) &&
            Objects.equals(umbral, that.umbral) &&
            Objects.equals(operador, that.operador) &&
            Objects.equals(nivelSeveridad, that.nivelSeveridad) &&
            Objects.equals(notificacionEnviada, that.notificacionEnviada) &&
            Objects.equals(canalUsado, that.canalUsado) &&
            Objects.equals(resuelto, that.resuelto) &&
            Objects.equals(fechaResolucion, that.fechaResolucion) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(inversorId, that.inversorId) &&
            Objects.equals(alertaConfigId, that.alertaConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            timestamp,
            nombreAlerta,
            mensaje,
            campoMonitorizado,
            valorDetectado,
            umbral,
            operador,
            nivelSeveridad,
            notificacionEnviada,
            canalUsado,
            resuelto,
            fechaResolucion,
            notas,
            inversorId,
            alertaConfigId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoAlertaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalNombreAlerta().map(f -> "nombreAlerta=" + f + ", ").orElse("") +
            optionalMensaje().map(f -> "mensaje=" + f + ", ").orElse("") +
            optionalCampoMonitorizado().map(f -> "campoMonitorizado=" + f + ", ").orElse("") +
            optionalValorDetectado().map(f -> "valorDetectado=" + f + ", ").orElse("") +
            optionalUmbral().map(f -> "umbral=" + f + ", ").orElse("") +
            optionalOperador().map(f -> "operador=" + f + ", ").orElse("") +
            optionalNivelSeveridad().map(f -> "nivelSeveridad=" + f + ", ").orElse("") +
            optionalNotificacionEnviada().map(f -> "notificacionEnviada=" + f + ", ").orElse("") +
            optionalCanalUsado().map(f -> "canalUsado=" + f + ", ").orElse("") +
            optionalResuelto().map(f -> "resuelto=" + f + ", ").orElse("") +
            optionalFechaResolucion().map(f -> "fechaResolucion=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalInversorId().map(f -> "inversorId=" + f + ", ").orElse("") +
            optionalAlertaConfigId().map(f -> "alertaConfigId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

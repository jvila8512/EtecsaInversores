package com.etecsa.monitor.service.criteria;

import com.etecsa.monitor.domain.enumeration.TipoDato;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.etecsa.monitor.domain.MapeoRegistro} entity. This class is used
 * in {@link com.etecsa.monitor.web.rest.MapeoRegistroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mapeo-registros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapeoRegistroCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoDato
     */
    public static class TipoDatoFilter extends Filter<TipoDato> {

        public TipoDatoFilter() {}

        public TipoDatoFilter(TipoDatoFilter filter) {
            super(filter);
        }

        @Override
        public TipoDatoFilter copy() {
            return new TipoDatoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter direccionRegistro;

    private StringFilter nombreRegistro;

    private StringFilter campoSql;

    private TipoDatoFilter tipoDato;

    private DoubleFilter escala;

    private StringFilter unidad;

    private BooleanFilter activo;

    private StringFilter descripcion;

    private LongFilter marcaId;

    private Boolean distinct;

    public MapeoRegistroCriteria() {}

    public MapeoRegistroCriteria(MapeoRegistroCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.direccionRegistro = other.optionalDireccionRegistro().map(IntegerFilter::copy).orElse(null);
        this.nombreRegistro = other.optionalNombreRegistro().map(StringFilter::copy).orElse(null);
        this.campoSql = other.optionalCampoSql().map(StringFilter::copy).orElse(null);
        this.tipoDato = other.optionalTipoDato().map(TipoDatoFilter::copy).orElse(null);
        this.escala = other.optionalEscala().map(DoubleFilter::copy).orElse(null);
        this.unidad = other.optionalUnidad().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.marcaId = other.optionalMarcaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MapeoRegistroCriteria copy() {
        return new MapeoRegistroCriteria(this);
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

    public IntegerFilter getDireccionRegistro() {
        return direccionRegistro;
    }

    public Optional<IntegerFilter> optionalDireccionRegistro() {
        return Optional.ofNullable(direccionRegistro);
    }

    public IntegerFilter direccionRegistro() {
        if (direccionRegistro == null) {
            setDireccionRegistro(new IntegerFilter());
        }
        return direccionRegistro;
    }

    public void setDireccionRegistro(IntegerFilter direccionRegistro) {
        this.direccionRegistro = direccionRegistro;
    }

    public StringFilter getNombreRegistro() {
        return nombreRegistro;
    }

    public Optional<StringFilter> optionalNombreRegistro() {
        return Optional.ofNullable(nombreRegistro);
    }

    public StringFilter nombreRegistro() {
        if (nombreRegistro == null) {
            setNombreRegistro(new StringFilter());
        }
        return nombreRegistro;
    }

    public void setNombreRegistro(StringFilter nombreRegistro) {
        this.nombreRegistro = nombreRegistro;
    }

    public StringFilter getCampoSql() {
        return campoSql;
    }

    public Optional<StringFilter> optionalCampoSql() {
        return Optional.ofNullable(campoSql);
    }

    public StringFilter campoSql() {
        if (campoSql == null) {
            setCampoSql(new StringFilter());
        }
        return campoSql;
    }

    public void setCampoSql(StringFilter campoSql) {
        this.campoSql = campoSql;
    }

    public TipoDatoFilter getTipoDato() {
        return tipoDato;
    }

    public Optional<TipoDatoFilter> optionalTipoDato() {
        return Optional.ofNullable(tipoDato);
    }

    public TipoDatoFilter tipoDato() {
        if (tipoDato == null) {
            setTipoDato(new TipoDatoFilter());
        }
        return tipoDato;
    }

    public void setTipoDato(TipoDatoFilter tipoDato) {
        this.tipoDato = tipoDato;
    }

    public DoubleFilter getEscala() {
        return escala;
    }

    public Optional<DoubleFilter> optionalEscala() {
        return Optional.ofNullable(escala);
    }

    public DoubleFilter escala() {
        if (escala == null) {
            setEscala(new DoubleFilter());
        }
        return escala;
    }

    public void setEscala(DoubleFilter escala) {
        this.escala = escala;
    }

    public StringFilter getUnidad() {
        return unidad;
    }

    public Optional<StringFilter> optionalUnidad() {
        return Optional.ofNullable(unidad);
    }

    public StringFilter unidad() {
        if (unidad == null) {
            setUnidad(new StringFilter());
        }
        return unidad;
    }

    public void setUnidad(StringFilter unidad) {
        this.unidad = unidad;
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
        final MapeoRegistroCriteria that = (MapeoRegistroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(direccionRegistro, that.direccionRegistro) &&
            Objects.equals(nombreRegistro, that.nombreRegistro) &&
            Objects.equals(campoSql, that.campoSql) &&
            Objects.equals(tipoDato, that.tipoDato) &&
            Objects.equals(escala, that.escala) &&
            Objects.equals(unidad, that.unidad) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(marcaId, that.marcaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            direccionRegistro,
            nombreRegistro,
            campoSql,
            tipoDato,
            escala,
            unidad,
            activo,
            descripcion,
            marcaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapeoRegistroCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDireccionRegistro().map(f -> "direccionRegistro=" + f + ", ").orElse("") +
            optionalNombreRegistro().map(f -> "nombreRegistro=" + f + ", ").orElse("") +
            optionalCampoSql().map(f -> "campoSql=" + f + ", ").orElse("") +
            optionalTipoDato().map(f -> "tipoDato=" + f + ", ").orElse("") +
            optionalEscala().map(f -> "escala=" + f + ", ").orElse("") +
            optionalUnidad().map(f -> "unidad=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalMarcaId().map(f -> "marcaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

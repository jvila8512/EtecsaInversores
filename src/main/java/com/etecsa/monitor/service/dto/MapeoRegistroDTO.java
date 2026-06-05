package com.etecsa.monitor.service.dto;

import com.etecsa.monitor.domain.enumeration.TipoDato;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.MapeoRegistro} entity.
 */
@Schema(
    description = "MapeoRegistro — direccion + escala de cada campo por marca.\nPermite soportar cualquier marca sin cambiar código Java."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapeoRegistroDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer direccionRegistro;

    @NotNull
    @Size(max = 100)
    private String nombreRegistro;

    @NotNull
    @Size(max = 60)
    private String campoSql;

    @NotNull
    private TipoDato tipoDato;

    private Double escala;

    @Size(max = 10)
    private String unidad;

    @NotNull
    private Boolean activo;

    @Size(max = 255)
    private String descripcion;

    private MarcaDTO marca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDireccionRegistro() {
        return direccionRegistro;
    }

    public void setDireccionRegistro(Integer direccionRegistro) {
        this.direccionRegistro = direccionRegistro;
    }

    public String getNombreRegistro() {
        return nombreRegistro;
    }

    public void setNombreRegistro(String nombreRegistro) {
        this.nombreRegistro = nombreRegistro;
    }

    public String getCampoSql() {
        return campoSql;
    }

    public void setCampoSql(String campoSql) {
        this.campoSql = campoSql;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Double getEscala() {
        return escala;
    }

    public void setEscala(Double escala) {
        this.escala = escala;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MarcaDTO getMarca() {
        return marca;
    }

    public void setMarca(MarcaDTO marca) {
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapeoRegistroDTO)) {
            return false;
        }

        MapeoRegistroDTO mapeoRegistroDTO = (MapeoRegistroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mapeoRegistroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapeoRegistroDTO{" +
            "id=" + getId() +
            ", direccionRegistro=" + getDireccionRegistro() +
            ", nombreRegistro='" + getNombreRegistro() + "'" +
            ", campoSql='" + getCampoSql() + "'" +
            ", tipoDato='" + getTipoDato() + "'" +
            ", escala=" + getEscala() +
            ", unidad='" + getUnidad() + "'" +
            ", activo='" + getActivo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", marca=" + getMarca() +
            "}";
    }
}

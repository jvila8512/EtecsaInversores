package com.etecsa.monitor.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.Marca} entity.
 */
@Schema(description = "Marca — fabricante con sus rangos de registros Modbus.\nFelicity: 0x1101–0x112A\nMawa:     0x1000–0x1050")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarcaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @Size(max = 100)
    private String fabricante;

    @NotNull
    private Integer registroInicio;

    @NotNull
    private Integer registroFin;

    private Integer registroInfoInicio;

    private Integer registroInfoFin;

    private Integer baudRateDefault;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private Boolean activaMarcaDefault;

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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Integer getRegistroInicio() {
        return registroInicio;
    }

    public void setRegistroInicio(Integer registroInicio) {
        this.registroInicio = registroInicio;
    }

    public Integer getRegistroFin() {
        return registroFin;
    }

    public void setRegistroFin(Integer registroFin) {
        this.registroFin = registroFin;
    }

    public Integer getRegistroInfoInicio() {
        return registroInfoInicio;
    }

    public void setRegistroInfoInicio(Integer registroInfoInicio) {
        this.registroInfoInicio = registroInfoInicio;
    }

    public Integer getRegistroInfoFin() {
        return registroInfoFin;
    }

    public void setRegistroInfoFin(Integer registroInfoFin) {
        this.registroInfoFin = registroInfoFin;
    }

    public Integer getBaudRateDefault() {
        return baudRateDefault;
    }

    public void setBaudRateDefault(Integer baudRateDefault) {
        this.baudRateDefault = baudRateDefault;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivaMarcaDefault() {
        return activaMarcaDefault;
    }

    public void setActivaMarcaDefault(Boolean activaMarcaDefault) {
        this.activaMarcaDefault = activaMarcaDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarcaDTO)) {
            return false;
        }

        MarcaDTO marcaDTO = (MarcaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marcaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarcaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fabricante='" + getFabricante() + "'" +
            ", registroInicio=" + getRegistroInicio() +
            ", registroFin=" + getRegistroFin() +
            ", registroInfoInicio=" + getRegistroInfoInicio() +
            ", registroInfoFin=" + getRegistroInfoFin() +
            ", baudRateDefault=" + getBaudRateDefault() +
            ", descripcion='" + getDescripcion() + "'" +
            ", activaMarcaDefault='" + getActivaMarcaDefault() + "'" +
            "}";
    }
}

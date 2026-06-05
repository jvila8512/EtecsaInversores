package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.ModeloInversor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModeloInversorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    @NotNull
    private Integer capacidadVa;

    @NotNull
    private Integer voltajeSistemaNom;

    @Size(max = 500)
    private String descripcion;

    private MarcaDTO marca;

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

    public Integer getCapacidadVa() {
        return capacidadVa;
    }

    public void setCapacidadVa(Integer capacidadVa) {
        this.capacidadVa = capacidadVa;
    }

    public Integer getVoltajeSistemaNom() {
        return voltajeSistemaNom;
    }

    public void setVoltajeSistemaNom(Integer voltajeSistemaNom) {
        this.voltajeSistemaNom = voltajeSistemaNom;
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
        if (!(o instanceof ModeloInversorDTO)) {
            return false;
        }

        ModeloInversorDTO modeloInversorDTO = (ModeloInversorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modeloInversorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModeloInversorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", capacidadVa=" + getCapacidadVa() +
            ", voltajeSistemaNom=" + getVoltajeSistemaNom() +
            ", descripcion='" + getDescripcion() + "'" +
            ", marca=" + getMarca() +
            "}";
    }
}

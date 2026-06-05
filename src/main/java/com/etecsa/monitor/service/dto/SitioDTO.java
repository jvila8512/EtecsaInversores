package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.Sitio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SitioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @Size(max = 200)
    private String ubicacion;

    @Size(max = 100)
    private String contacto;

    @Size(max = 20)
    private String telefono;

    @NotNull
    private Boolean activo;

    private LocalDate fechaInstalacion;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(LocalDate fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SitioDTO)) {
            return false;
        }

        SitioDTO sitioDTO = (SitioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sitioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SitioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", contacto='" + getContacto() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaInstalacion='" + getFechaInstalacion() + "'" +
            "}";
    }
}

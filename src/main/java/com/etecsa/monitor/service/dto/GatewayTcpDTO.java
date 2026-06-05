package com.etecsa.monitor.service.dto;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.GatewayTcp} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GatewayTcpDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @Size(max = 200)
    private String ubicacion;

    @NotNull
    @Size(max = 45)
    private String direccionIp;

    @NotNull
    private Integer puerto;

    @NotNull
    private Boolean activo;

    private EstadoDispositivo estadoConexion;

    private Instant ultimaLectura;

    @Size(max = 500)
    private String ultimoError;

    private Integer tiempoEsperaMs;

    private Integer reintentos;

    private SitioDTO sitio;

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

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public EstadoDispositivo getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(EstadoDispositivo estadoConexion) {
        this.estadoConexion = estadoConexion;
    }

    public Instant getUltimaLectura() {
        return ultimaLectura;
    }

    public void setUltimaLectura(Instant ultimaLectura) {
        this.ultimaLectura = ultimaLectura;
    }

    public String getUltimoError() {
        return ultimoError;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }

    public Integer getTiempoEsperaMs() {
        return tiempoEsperaMs;
    }

    public void setTiempoEsperaMs(Integer tiempoEsperaMs) {
        this.tiempoEsperaMs = tiempoEsperaMs;
    }

    public Integer getReintentos() {
        return reintentos;
    }

    public void setReintentos(Integer reintentos) {
        this.reintentos = reintentos;
    }

    public SitioDTO getSitio() {
        return sitio;
    }

    public void setSitio(SitioDTO sitio) {
        this.sitio = sitio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GatewayTcpDTO)) {
            return false;
        }

        GatewayTcpDTO gatewayTcpDTO = (GatewayTcpDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gatewayTcpDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GatewayTcpDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", direccionIp='" + getDireccionIp() + "'" +
            ", puerto=" + getPuerto() +
            ", activo='" + getActivo() + "'" +
            ", estadoConexion='" + getEstadoConexion() + "'" +
            ", ultimaLectura='" + getUltimaLectura() + "'" +
            ", ultimoError='" + getUltimoError() + "'" +
            ", tiempoEsperaMs=" + getTiempoEsperaMs() +
            ", reintentos=" + getReintentos() +
            ", sitio=" + getSitio() +
            "}";
    }
}

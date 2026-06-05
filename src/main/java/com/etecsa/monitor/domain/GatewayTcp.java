package com.etecsa.monitor.domain;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GatewayTcp.
 */
@Entity
@Table(name = "gateway_tcp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GatewayTcp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Size(max = 200)
    @Column(name = "ubicacion", length = 200)
    private String ubicacion;

    @NotNull
    @Size(max = 45)
    @Column(name = "direccion_ip", length = 45, nullable = false)
    private String direccionIp;

    @NotNull
    @Column(name = "puerto", nullable = false)
    private Integer puerto;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conexion")
    private EstadoDispositivo estadoConexion;

    @Column(name = "ultima_lectura")
    private Instant ultimaLectura;

    @Size(max = 500)
    @Column(name = "ultimo_error", length = 500)
    private String ultimoError;

    @Column(name = "tiempo_espera_ms")
    private Integer tiempoEsperaMs;

    @Column(name = "reintentos")
    private Integer reintentos;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sitio sitio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GatewayTcp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public GatewayTcp nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public GatewayTcp descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public GatewayTcp ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDireccionIp() {
        return this.direccionIp;
    }

    public GatewayTcp direccionIp(String direccionIp) {
        this.setDireccionIp(direccionIp);
        return this;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public Integer getPuerto() {
        return this.puerto;
    }

    public GatewayTcp puerto(Integer puerto) {
        this.setPuerto(puerto);
        return this;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public GatewayTcp activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public EstadoDispositivo getEstadoConexion() {
        return this.estadoConexion;
    }

    public GatewayTcp estadoConexion(EstadoDispositivo estadoConexion) {
        this.setEstadoConexion(estadoConexion);
        return this;
    }

    public void setEstadoConexion(EstadoDispositivo estadoConexion) {
        this.estadoConexion = estadoConexion;
    }

    public Instant getUltimaLectura() {
        return this.ultimaLectura;
    }

    public GatewayTcp ultimaLectura(Instant ultimaLectura) {
        this.setUltimaLectura(ultimaLectura);
        return this;
    }

    public void setUltimaLectura(Instant ultimaLectura) {
        this.ultimaLectura = ultimaLectura;
    }

    public String getUltimoError() {
        return this.ultimoError;
    }

    public GatewayTcp ultimoError(String ultimoError) {
        this.setUltimoError(ultimoError);
        return this;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }

    public Integer getTiempoEsperaMs() {
        return this.tiempoEsperaMs;
    }

    public GatewayTcp tiempoEsperaMs(Integer tiempoEsperaMs) {
        this.setTiempoEsperaMs(tiempoEsperaMs);
        return this;
    }

    public void setTiempoEsperaMs(Integer tiempoEsperaMs) {
        this.tiempoEsperaMs = tiempoEsperaMs;
    }

    public Integer getReintentos() {
        return this.reintentos;
    }

    public GatewayTcp reintentos(Integer reintentos) {
        this.setReintentos(reintentos);
        return this;
    }

    public void setReintentos(Integer reintentos) {
        this.reintentos = reintentos;
    }

    public Sitio getSitio() {
        return this.sitio;
    }

    public void setSitio(Sitio sitio) {
        this.sitio = sitio;
    }

    public GatewayTcp sitio(Sitio sitio) {
        this.setSitio(sitio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GatewayTcp)) {
            return false;
        }
        return getId() != null && getId().equals(((GatewayTcp) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GatewayTcp{" +
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
            "}";
    }
}

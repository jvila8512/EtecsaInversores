package com.etecsa.monitor.domain;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.domain.enumeration.TipoConexion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inversor.
 */
@Entity
@Table(name = "inversor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inversor implements Serializable {

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

    @Size(max = 50)
    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @NotNull
    @Column(name = "es_master", nullable = false)
    private Boolean esMaster;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conexion", nullable = false)
    private TipoConexion tipoConexion;

    @Size(max = 50)
    @Column(name = "puerto_serial", length = 50)
    private String puertoSerial;

    @NotNull
    @Column(name = "slave_id", nullable = false)
    private Integer slaveId;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_dispositivo")
    private EstadoDispositivo estadoDispositivo;

    @Column(name = "ultima_lectura")
    private Instant ultimaLectura;

    @Size(max = 500)
    @Column(name = "ultimo_error", length = 500)
    private String ultimoError;

    @Column(name = "periodo_muestreo_seg")
    private Integer periodoMuestreoSeg;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Column(name = "ultima_conexion")
    private Instant ultimaConexion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sitio sitio;

    @ManyToOne(fetch = FetchType.LAZY)
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "marca" }, allowSetters = true)
    private ModeloInversor modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio" }, allowSetters = true)
    private GatewayTcp gateway;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio", "marca", "modelo", "gateway", "masterReferencia" }, allowSetters = true)
    private Inversor masterReferencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inversor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Inversor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public Inversor numeroSerie(String numeroSerie) {
        this.setNumeroSerie(numeroSerie);
        return this;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Boolean getEsMaster() {
        return this.esMaster;
    }

    public Inversor esMaster(Boolean esMaster) {
        this.setEsMaster(esMaster);
        return this;
    }

    public void setEsMaster(Boolean esMaster) {
        this.esMaster = esMaster;
    }

    public TipoConexion getTipoConexion() {
        return this.tipoConexion;
    }

    public Inversor tipoConexion(TipoConexion tipoConexion) {
        this.setTipoConexion(tipoConexion);
        return this;
    }

    public void setTipoConexion(TipoConexion tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public String getPuertoSerial() {
        return this.puertoSerial;
    }

    public Inversor puertoSerial(String puertoSerial) {
        this.setPuertoSerial(puertoSerial);
        return this;
    }

    public void setPuertoSerial(String puertoSerial) {
        this.puertoSerial = puertoSerial;
    }

    public Integer getSlaveId() {
        return this.slaveId;
    }

    public Inversor slaveId(Integer slaveId) {
        this.setSlaveId(slaveId);
        return this;
    }

    public void setSlaveId(Integer slaveId) {
        this.slaveId = slaveId;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Inversor activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public EstadoDispositivo getEstadoDispositivo() {
        return this.estadoDispositivo;
    }

    public Inversor estadoDispositivo(EstadoDispositivo estadoDispositivo) {
        this.setEstadoDispositivo(estadoDispositivo);
        return this;
    }

    public void setEstadoDispositivo(EstadoDispositivo estadoDispositivo) {
        this.estadoDispositivo = estadoDispositivo;
    }

    public Instant getUltimaLectura() {
        return this.ultimaLectura;
    }

    public Inversor ultimaLectura(Instant ultimaLectura) {
        this.setUltimaLectura(ultimaLectura);
        return this;
    }

    public void setUltimaLectura(Instant ultimaLectura) {
        this.ultimaLectura = ultimaLectura;
    }

    public String getUltimoError() {
        return this.ultimoError;
    }

    public Inversor ultimoError(String ultimoError) {
        this.setUltimoError(ultimoError);
        return this;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }

    public Integer getPeriodoMuestreoSeg() {
        return this.periodoMuestreoSeg;
    }

    public Inversor periodoMuestreoSeg(Integer periodoMuestreoSeg) {
        this.setPeriodoMuestreoSeg(periodoMuestreoSeg);
        return this;
    }

    public void setPeriodoMuestreoSeg(Integer periodoMuestreoSeg) {
        this.periodoMuestreoSeg = periodoMuestreoSeg;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Inversor fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getUltimaConexion() {
        return this.ultimaConexion;
    }

    public Inversor ultimaConexion(Instant ultimaConexion) {
        this.setUltimaConexion(ultimaConexion);
        return this;
    }

    public void setUltimaConexion(Instant ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public Sitio getSitio() {
        return this.sitio;
    }

    public void setSitio(Sitio sitio) {
        this.sitio = sitio;
    }

    public Inversor sitio(Sitio sitio) {
        this.setSitio(sitio);
        return this;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Inversor marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    public ModeloInversor getModelo() {
        return this.modelo;
    }

    public void setModelo(ModeloInversor modeloInversor) {
        this.modelo = modeloInversor;
    }

    public Inversor modelo(ModeloInversor modeloInversor) {
        this.setModelo(modeloInversor);
        return this;
    }

    public GatewayTcp getGateway() {
        return this.gateway;
    }

    public void setGateway(GatewayTcp gatewayTcp) {
        this.gateway = gatewayTcp;
    }

    public Inversor gateway(GatewayTcp gatewayTcp) {
        this.setGateway(gatewayTcp);
        return this;
    }

    public Inversor getMasterReferencia() {
        return this.masterReferencia;
    }

    public void setMasterReferencia(Inversor inversor) {
        this.masterReferencia = inversor;
    }

    public Inversor masterReferencia(Inversor inversor) {
        this.setMasterReferencia(inversor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inversor)) {
            return false;
        }
        return getId() != null && getId().equals(((Inversor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inversor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", esMaster='" + getEsMaster() + "'" +
            ", tipoConexion='" + getTipoConexion() + "'" +
            ", puertoSerial='" + getPuertoSerial() + "'" +
            ", slaveId=" + getSlaveId() +
            ", activo='" + getActivo() + "'" +
            ", estadoDispositivo='" + getEstadoDispositivo() + "'" +
            ", ultimaLectura='" + getUltimaLectura() + "'" +
            ", ultimoError='" + getUltimoError() + "'" +
            ", periodoMuestreoSeg=" + getPeriodoMuestreoSeg() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", ultimaConexion='" + getUltimaConexion() + "'" +
            "}";
    }
}

package com.etecsa.monitor.service.dto;

import com.etecsa.monitor.domain.enumeration.EstadoDispositivo;
import com.etecsa.monitor.domain.enumeration.TipoConexion;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.Inversor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InversorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @Size(max = 50)
    private String numeroSerie;

    @NotNull
    private Boolean esMaster;

    @NotNull
    private TipoConexion tipoConexion;

    @Size(max = 50)
    private String puertoSerial;

    @NotNull
    private Integer slaveId;

    @NotNull
    private Boolean activo;

    private EstadoDispositivo estadoDispositivo;

    private Instant ultimaLectura;

    @Size(max = 500)
    private String ultimoError;

    private Integer periodoMuestreoSeg;

    @NotNull
    private Instant fechaRegistro;

    private Instant ultimaConexion;

    private SitioDTO sitio;

    private MarcaDTO marca;

    private ModeloInversorDTO modelo;

    private GatewayTcpDTO gateway;

    private InversorDTO masterReferencia;

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

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Boolean getEsMaster() {
        return esMaster;
    }

    public void setEsMaster(Boolean esMaster) {
        this.esMaster = esMaster;
    }

    public TipoConexion getTipoConexion() {
        return tipoConexion;
    }

    public void setTipoConexion(TipoConexion tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public String getPuertoSerial() {
        return puertoSerial;
    }

    public void setPuertoSerial(String puertoSerial) {
        this.puertoSerial = puertoSerial;
    }

    public Integer getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(Integer slaveId) {
        this.slaveId = slaveId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public EstadoDispositivo getEstadoDispositivo() {
        return estadoDispositivo;
    }

    public void setEstadoDispositivo(EstadoDispositivo estadoDispositivo) {
        this.estadoDispositivo = estadoDispositivo;
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

    public Integer getPeriodoMuestreoSeg() {
        return periodoMuestreoSeg;
    }

    public void setPeriodoMuestreoSeg(Integer periodoMuestreoSeg) {
        this.periodoMuestreoSeg = periodoMuestreoSeg;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(Instant ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public SitioDTO getSitio() {
        return sitio;
    }

    public void setSitio(SitioDTO sitio) {
        this.sitio = sitio;
    }

    public MarcaDTO getMarca() {
        return marca;
    }

    public void setMarca(MarcaDTO marca) {
        this.marca = marca;
    }

    public ModeloInversorDTO getModelo() {
        return modelo;
    }

    public void setModelo(ModeloInversorDTO modelo) {
        this.modelo = modelo;
    }

    public GatewayTcpDTO getGateway() {
        return gateway;
    }

    public void setGateway(GatewayTcpDTO gateway) {
        this.gateway = gateway;
    }

    public InversorDTO getMasterReferencia() {
        return masterReferencia;
    }

    public void setMasterReferencia(InversorDTO masterReferencia) {
        this.masterReferencia = masterReferencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InversorDTO)) {
            return false;
        }

        InversorDTO inversorDTO = (InversorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inversorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InversorDTO{" +
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
            ", sitio=" + getSitio() +
            ", marca=" + getMarca() +
            ", modelo=" + getModelo() +
            ", gateway=" + getGateway() +
            ", masterReferencia=" + getMasterReferencia() +
            "}";
    }
}

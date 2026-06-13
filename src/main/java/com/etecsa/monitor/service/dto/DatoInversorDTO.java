package com.etecsa.monitor.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.etecsa.monitor.domain.DatoInversor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoInversorDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant tiempo;

    private Double bateriaVoltajeV;

    private Double bateriaCorrienteA;

    private Double bateriaPotenciaW;

    private Double pvVoltajeV;

    private Double pvPotenciaW;

    private Double salidaVoltajeV;

    private Double salidaPotenciaW;

    private Double salidaAparenteVa;

    private Integer cargaPorcentaje;

    private Double redVoltajeV;

    private Double redFrecuenciaHz;

    @Size(max = 20)
    private String modoOperacion;

    private Integer codigoFallo;

    private Double eficienciaPct;

    private Double consumoRedW;

    private Boolean bateriaCargando;

    private Boolean pvActivo;

    private Boolean redActiva;

    private Boolean crcValido;

  private Integer latenciaMs;

  private Double pvCorrienteA;

  private Double salidaCorrienteA;

  private Double salidaFrecuenciaHz;

  private Double temperaturaC;

  private Double busVoltajeV;

  private Integer socPct;

  private InversorDTO inversor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTiempo() {
        return tiempo;
    }

    public void setTiempo(Instant tiempo) {
        this.tiempo = tiempo;
    }

    public Double getBateriaVoltajeV() {
        return bateriaVoltajeV;
    }

    public void setBateriaVoltajeV(Double bateriaVoltajeV) {
        this.bateriaVoltajeV = bateriaVoltajeV;
    }

    public Double getBateriaCorrienteA() {
        return bateriaCorrienteA;
    }

    public void setBateriaCorrienteA(Double bateriaCorrienteA) {
        this.bateriaCorrienteA = bateriaCorrienteA;
    }

    public Double getBateriaPotenciaW() {
        return bateriaPotenciaW;
    }

    public void setBateriaPotenciaW(Double bateriaPotenciaW) {
        this.bateriaPotenciaW = bateriaPotenciaW;
    }

    public Double getPvVoltajeV() {
        return pvVoltajeV;
    }

    public void setPvVoltajeV(Double pvVoltajeV) {
        this.pvVoltajeV = pvVoltajeV;
    }

    public Double getPvPotenciaW() {
        return pvPotenciaW;
    }

    public void setPvPotenciaW(Double pvPotenciaW) {
        this.pvPotenciaW = pvPotenciaW;
    }

    public Double getSalidaVoltajeV() {
        return salidaVoltajeV;
    }

    public void setSalidaVoltajeV(Double salidaVoltajeV) {
        this.salidaVoltajeV = salidaVoltajeV;
    }

    public Double getSalidaPotenciaW() {
        return salidaPotenciaW;
    }

    public void setSalidaPotenciaW(Double salidaPotenciaW) {
        this.salidaPotenciaW = salidaPotenciaW;
    }

    public Double getSalidaAparenteVa() {
        return salidaAparenteVa;
    }

    public void setSalidaAparenteVa(Double salidaAparenteVa) {
        this.salidaAparenteVa = salidaAparenteVa;
    }

    public Integer getCargaPorcentaje() {
        return cargaPorcentaje;
    }

    public void setCargaPorcentaje(Integer cargaPorcentaje) {
        this.cargaPorcentaje = cargaPorcentaje;
    }

    public Double getRedVoltajeV() {
        return redVoltajeV;
    }

    public void setRedVoltajeV(Double redVoltajeV) {
        this.redVoltajeV = redVoltajeV;
    }

    public Double getRedFrecuenciaHz() {
        return redFrecuenciaHz;
    }

    public void setRedFrecuenciaHz(Double redFrecuenciaHz) {
        this.redFrecuenciaHz = redFrecuenciaHz;
    }

    public String getModoOperacion() {
        return modoOperacion;
    }

    public void setModoOperacion(String modoOperacion) {
        this.modoOperacion = modoOperacion;
    }

    public Integer getCodigoFallo() {
        return codigoFallo;
    }

    public void setCodigoFallo(Integer codigoFallo) {
        this.codigoFallo = codigoFallo;
    }

    public Double getEficienciaPct() {
        return eficienciaPct;
    }

    public void setEficienciaPct(Double eficienciaPct) {
        this.eficienciaPct = eficienciaPct;
    }

    public Double getConsumoRedW() {
        return consumoRedW;
    }

    public void setConsumoRedW(Double consumoRedW) {
        this.consumoRedW = consumoRedW;
    }

    public Boolean getBateriaCargando() {
        return bateriaCargando;
    }

    public void setBateriaCargando(Boolean bateriaCargando) {
        this.bateriaCargando = bateriaCargando;
    }

    public Boolean getPvActivo() {
        return pvActivo;
    }

    public void setPvActivo(Boolean pvActivo) {
        this.pvActivo = pvActivo;
    }

    public Boolean getRedActiva() {
        return redActiva;
    }

    public void setRedActiva(Boolean redActiva) {
        this.redActiva = redActiva;
    }

    public Boolean getCrcValido() {
        return crcValido;
    }

    public void setCrcValido(Boolean crcValido) {
        this.crcValido = crcValido;
    }

    public Integer getLatenciaMs() {
        return latenciaMs;
    }

  public void setLatenciaMs(Integer latenciaMs) {
    this.latenciaMs = latenciaMs;
  }

  public Double getPvCorrienteA() {
    return pvCorrienteA;
  }

  public void setPvCorrienteA(Double pvCorrienteA) {
    this.pvCorrienteA = pvCorrienteA;
  }

  public Double getSalidaCorrienteA() {
    return salidaCorrienteA;
  }

  public void setSalidaCorrienteA(Double salidaCorrienteA) {
    this.salidaCorrienteA = salidaCorrienteA;
  }

  public Double getSalidaFrecuenciaHz() {
    return salidaFrecuenciaHz;
  }

  public void setSalidaFrecuenciaHz(Double salidaFrecuenciaHz) {
    this.salidaFrecuenciaHz = salidaFrecuenciaHz;
  }

  public Double getTemperaturaC() {
    return temperaturaC;
  }

  public void setTemperaturaC(Double temperaturaC) {
    this.temperaturaC = temperaturaC;
  }

  public Double getBusVoltajeV() {
    return busVoltajeV;
  }

  public void setBusVoltajeV(Double busVoltajeV) {
    this.busVoltajeV = busVoltajeV;
  }

  public Integer getSocPct() {
    return socPct;
  }

  public void setSocPct(Integer socPct) {
    this.socPct = socPct;
  }

  public InversorDTO getInversor() {
        return inversor;
    }

    public void setInversor(InversorDTO inversor) {
        this.inversor = inversor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatoInversorDTO)) {
            return false;
        }

        DatoInversorDTO datoInversorDTO = (DatoInversorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, datoInversorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoInversorDTO{" +
            "id=" + getId() +
            ", tiempo='" + getTiempo() + "'" +
            ", bateriaVoltajeV=" + getBateriaVoltajeV() +
            ", bateriaCorrienteA=" + getBateriaCorrienteA() +
            ", bateriaPotenciaW=" + getBateriaPotenciaW() +
            ", pvVoltajeV=" + getPvVoltajeV() +
            ", pvPotenciaW=" + getPvPotenciaW() +
            ", salidaVoltajeV=" + getSalidaVoltajeV() +
            ", salidaPotenciaW=" + getSalidaPotenciaW() +
            ", salidaAparenteVa=" + getSalidaAparenteVa() +
            ", cargaPorcentaje=" + getCargaPorcentaje() +
            ", redVoltajeV=" + getRedVoltajeV() +
            ", redFrecuenciaHz=" + getRedFrecuenciaHz() +
            ", modoOperacion='" + getModoOperacion() + "'" +
            ", codigoFallo=" + getCodigoFallo() +
            ", eficienciaPct=" + getEficienciaPct() +
            ", consumoRedW=" + getConsumoRedW() +
            ", bateriaCargando='" + getBateriaCargando() + "'" +
            ", pvActivo='" + getPvActivo() + "'" +
            ", redActiva='" + getRedActiva() + "'" +
            ", crcValido='" + getCrcValido() + "'" +
      ", latenciaMs=" + getLatenciaMs() +
      ", pvCorrienteA=" + getPvCorrienteA() +
      ", salidaCorrienteA=" + getSalidaCorrienteA() +
      ", salidaFrecuenciaHz=" + getSalidaFrecuenciaHz() +
      ", temperaturaC=" + getTemperaturaC() +
      ", busVoltajeV=" + getBusVoltajeV() +
      ", socPct=" + getSocPct() +
      ", inversor=" + getInversor() +
            "}";
    }
}

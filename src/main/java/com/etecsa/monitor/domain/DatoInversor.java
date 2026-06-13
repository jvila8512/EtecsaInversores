package com.etecsa.monitor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DatoInversor.
 */
@Entity
@Table(name = "dato_inversor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DatoInversor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tiempo", nullable = false)
    private Instant tiempo;

    @Column(name = "bateria_voltaje_v")
    private Double bateriaVoltajeV;

    @Column(name = "bateria_corriente_a")
    private Double bateriaCorrienteA;

    @Column(name = "bateria_potencia_w")
    private Double bateriaPotenciaW;

    @Column(name = "pv_voltaje_v")
    private Double pvVoltajeV;

    @Column(name = "pv_potencia_w")
    private Double pvPotenciaW;

    @Column(name = "salida_voltaje_v")
    private Double salidaVoltajeV;

    @Column(name = "salida_potencia_w")
    private Double salidaPotenciaW;

    @Column(name = "salida_aparente_va")
    private Double salidaAparenteVa;

    @Column(name = "carga_porcentaje")
    private Integer cargaPorcentaje;

    @Column(name = "red_voltaje_v")
    private Double redVoltajeV;

    @Column(name = "red_frecuencia_hz")
    private Double redFrecuenciaHz;

    @Size(max = 20)
    @Column(name = "modo_operacion", length = 20)
    private String modoOperacion;

    @Column(name = "codigo_fallo")
    private Integer codigoFallo;

    @Column(name = "eficiencia_pct")
    private Double eficienciaPct;

    @Column(name = "consumo_red_w")
    private Double consumoRedW;

    @Column(name = "bateria_cargando")
    private Boolean bateriaCargando;

    @Column(name = "pv_activo")
    private Boolean pvActivo;

    @Column(name = "red_activa")
    private Boolean redActiva;

    @Column(name = "crc_valido")
    private Boolean crcValido;

    @Column(name = "latencia_ms")
    private Integer latenciaMs;

    @Column(name = "pv_corriente_a")
    private Double pvCorrienteA;

    @Column(name = "salida_corriente_a")
    private Double salidaCorrienteA;

    @Column(name = "salida_frecuencia_hz")
    private Double salidaFrecuenciaHz;

    @Column(name = "temperatura_c")
    private Double temperaturaC;

    @Column(name = "bus_voltaje_v")
    private Double busVoltajeV;

    @Column(name = "soc_pct")
    private Integer socPct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitio", "marca", "modelo", "gateway", "masterReferencia" }, allowSetters = true)
    private Inversor inversor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DatoInversor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTiempo() {
        return this.tiempo;
    }

    public DatoInversor tiempo(Instant tiempo) {
        this.setTiempo(tiempo);
        return this;
    }

    public void setTiempo(Instant tiempo) {
        this.tiempo = tiempo;
    }

    public Double getBateriaVoltajeV() {
        return this.bateriaVoltajeV;
    }

    public DatoInversor bateriaVoltajeV(Double bateriaVoltajeV) {
        this.setBateriaVoltajeV(bateriaVoltajeV);
        return this;
    }

    public void setBateriaVoltajeV(Double bateriaVoltajeV) {
        this.bateriaVoltajeV = bateriaVoltajeV;
    }

    public Double getBateriaCorrienteA() {
        return this.bateriaCorrienteA;
    }

    public DatoInversor bateriaCorrienteA(Double bateriaCorrienteA) {
        this.setBateriaCorrienteA(bateriaCorrienteA);
        return this;
    }

    public void setBateriaCorrienteA(Double bateriaCorrienteA) {
        this.bateriaCorrienteA = bateriaCorrienteA;
    }

    public Double getBateriaPotenciaW() {
        return this.bateriaPotenciaW;
    }

    public DatoInversor bateriaPotenciaW(Double bateriaPotenciaW) {
        this.setBateriaPotenciaW(bateriaPotenciaW);
        return this;
    }

    public void setBateriaPotenciaW(Double bateriaPotenciaW) {
        this.bateriaPotenciaW = bateriaPotenciaW;
    }

    public Double getPvVoltajeV() {
        return this.pvVoltajeV;
    }

    public DatoInversor pvVoltajeV(Double pvVoltajeV) {
        this.setPvVoltajeV(pvVoltajeV);
        return this;
    }

    public void setPvVoltajeV(Double pvVoltajeV) {
        this.pvVoltajeV = pvVoltajeV;
    }

    public Double getPvPotenciaW() {
        return this.pvPotenciaW;
    }

    public DatoInversor pvPotenciaW(Double pvPotenciaW) {
        this.setPvPotenciaW(pvPotenciaW);
        return this;
    }

    public void setPvPotenciaW(Double pvPotenciaW) {
        this.pvPotenciaW = pvPotenciaW;
    }

    public Double getSalidaVoltajeV() {
        return this.salidaVoltajeV;
    }

    public DatoInversor salidaVoltajeV(Double salidaVoltajeV) {
        this.setSalidaVoltajeV(salidaVoltajeV);
        return this;
    }

    public void setSalidaVoltajeV(Double salidaVoltajeV) {
        this.salidaVoltajeV = salidaVoltajeV;
    }

    public Double getSalidaPotenciaW() {
        return this.salidaPotenciaW;
    }

    public DatoInversor salidaPotenciaW(Double salidaPotenciaW) {
        this.setSalidaPotenciaW(salidaPotenciaW);
        return this;
    }

    public void setSalidaPotenciaW(Double salidaPotenciaW) {
        this.salidaPotenciaW = salidaPotenciaW;
    }

    public Double getSalidaAparenteVa() {
        return this.salidaAparenteVa;
    }

    public DatoInversor salidaAparenteVa(Double salidaAparenteVa) {
        this.setSalidaAparenteVa(salidaAparenteVa);
        return this;
    }

    public void setSalidaAparenteVa(Double salidaAparenteVa) {
        this.salidaAparenteVa = salidaAparenteVa;
    }

    public Integer getCargaPorcentaje() {
        return this.cargaPorcentaje;
    }

    public DatoInversor cargaPorcentaje(Integer cargaPorcentaje) {
        this.setCargaPorcentaje(cargaPorcentaje);
        return this;
    }

    public void setCargaPorcentaje(Integer cargaPorcentaje) {
        this.cargaPorcentaje = cargaPorcentaje;
    }

    public Double getRedVoltajeV() {
        return this.redVoltajeV;
    }

    public DatoInversor redVoltajeV(Double redVoltajeV) {
        this.setRedVoltajeV(redVoltajeV);
        return this;
    }

    public void setRedVoltajeV(Double redVoltajeV) {
        this.redVoltajeV = redVoltajeV;
    }

    public Double getRedFrecuenciaHz() {
        return this.redFrecuenciaHz;
    }

    public DatoInversor redFrecuenciaHz(Double redFrecuenciaHz) {
        this.setRedFrecuenciaHz(redFrecuenciaHz);
        return this;
    }

    public void setRedFrecuenciaHz(Double redFrecuenciaHz) {
        this.redFrecuenciaHz = redFrecuenciaHz;
    }

    public String getModoOperacion() {
        return this.modoOperacion;
    }

    public DatoInversor modoOperacion(String modoOperacion) {
        this.setModoOperacion(modoOperacion);
        return this;
    }

    public void setModoOperacion(String modoOperacion) {
        this.modoOperacion = modoOperacion;
    }

    public Integer getCodigoFallo() {
        return this.codigoFallo;
    }

    public DatoInversor codigoFallo(Integer codigoFallo) {
        this.setCodigoFallo(codigoFallo);
        return this;
    }

    public void setCodigoFallo(Integer codigoFallo) {
        this.codigoFallo = codigoFallo;
    }

    public Double getEficienciaPct() {
        return this.eficienciaPct;
    }

    public DatoInversor eficienciaPct(Double eficienciaPct) {
        this.setEficienciaPct(eficienciaPct);
        return this;
    }

    public void setEficienciaPct(Double eficienciaPct) {
        this.eficienciaPct = eficienciaPct;
    }

    public Double getConsumoRedW() {
        return this.consumoRedW;
    }

    public DatoInversor consumoRedW(Double consumoRedW) {
        this.setConsumoRedW(consumoRedW);
        return this;
    }

    public void setConsumoRedW(Double consumoRedW) {
        this.consumoRedW = consumoRedW;
    }

    public Boolean getBateriaCargando() {
        return this.bateriaCargando;
    }

    public DatoInversor bateriaCargando(Boolean bateriaCargando) {
        this.setBateriaCargando(bateriaCargando);
        return this;
    }

    public void setBateriaCargando(Boolean bateriaCargando) {
        this.bateriaCargando = bateriaCargando;
    }

    public Boolean getPvActivo() {
        return this.pvActivo;
    }

    public DatoInversor pvActivo(Boolean pvActivo) {
        this.setPvActivo(pvActivo);
        return this;
    }

    public void setPvActivo(Boolean pvActivo) {
        this.pvActivo = pvActivo;
    }

    public Boolean getRedActiva() {
        return this.redActiva;
    }

    public DatoInversor redActiva(Boolean redActiva) {
        this.setRedActiva(redActiva);
        return this;
    }

    public void setRedActiva(Boolean redActiva) {
        this.redActiva = redActiva;
    }

    public Boolean getCrcValido() {
        return this.crcValido;
    }

    public DatoInversor crcValido(Boolean crcValido) {
        this.setCrcValido(crcValido);
        return this;
    }

    public void setCrcValido(Boolean crcValido) {
        this.crcValido = crcValido;
    }

    public Integer getLatenciaMs() {
        return this.latenciaMs;
    }

    public DatoInversor latenciaMs(Integer latenciaMs) {
        this.setLatenciaMs(latenciaMs);
        return this;
    }

  public void setLatenciaMs(Integer latenciaMs) {
    this.latenciaMs = latenciaMs;
  }

  public Double getPvCorrienteA() {
    return this.pvCorrienteA;
  }

  public DatoInversor pvCorrienteA(Double pvCorrienteA) {
    this.setPvCorrienteA(pvCorrienteA);
    return this;
  }

  public void setPvCorrienteA(Double pvCorrienteA) {
    this.pvCorrienteA = pvCorrienteA;
  }

  public Double getSalidaCorrienteA() {
    return this.salidaCorrienteA;
  }

  public DatoInversor salidaCorrienteA(Double salidaCorrienteA) {
    this.setSalidaCorrienteA(salidaCorrienteA);
    return this;
  }

  public void setSalidaCorrienteA(Double salidaCorrienteA) {
    this.salidaCorrienteA = salidaCorrienteA;
  }

  public Double getSalidaFrecuenciaHz() {
    return this.salidaFrecuenciaHz;
  }

  public DatoInversor salidaFrecuenciaHz(Double salidaFrecuenciaHz) {
    this.setSalidaFrecuenciaHz(salidaFrecuenciaHz);
    return this;
  }

  public void setSalidaFrecuenciaHz(Double salidaFrecuenciaHz) {
    this.salidaFrecuenciaHz = salidaFrecuenciaHz;
  }

  public Double getTemperaturaC() {
    return this.temperaturaC;
  }

  public DatoInversor temperaturaC(Double temperaturaC) {
    this.setTemperaturaC(temperaturaC);
    return this;
  }

  public void setTemperaturaC(Double temperaturaC) {
    this.temperaturaC = temperaturaC;
  }

  public Double getBusVoltajeV() {
    return this.busVoltajeV;
  }

  public DatoInversor busVoltajeV(Double busVoltajeV) {
    this.setBusVoltajeV(busVoltajeV);
    return this;
  }

  public void setBusVoltajeV(Double busVoltajeV) {
    this.busVoltajeV = busVoltajeV;
  }

  public Integer getSocPct() {
    return this.socPct;
  }

  public DatoInversor socPct(Integer socPct) {
    this.setSocPct(socPct);
    return this;
  }

  public void setSocPct(Integer socPct) {
    this.socPct = socPct;
  }

    public Inversor getInversor() {
        return this.inversor;
    }

    public DatoInversor inversor(Inversor inversor) {
        this.setInversor(inversor);
        return this;
    }

    public void setInversor(Inversor inversor) {
        this.inversor = inversor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatoInversor)) {
            return false;
        }
        return getId() != null && getId().equals(((DatoInversor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DatoInversor{" +
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
      "}";
    }
}

package com.etecsa.monitor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Marca — fabricante con sus rangos de registros Modbus.
 * Felicity: 0x1101–0x112A
 * Mawa:     0x1000–0x1050
 */
@Entity
@Table(name = "marca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "fabricante", length = 100)
    private String fabricante;

    @NotNull
    @Column(name = "registro_inicio", nullable = false)
    private Integer registroInicio;

    @NotNull
    @Column(name = "registro_fin", nullable = false)
    private Integer registroFin;

    @Column(name = "registro_info_inicio")
    private Integer registroInfoInicio;

    @Column(name = "registro_info_fin")
    private Integer registroInfoFin;

    @Column(name = "baud_rate_default")
    private Integer baudRateDefault;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull
    @Column(name = "activa_marca_default", nullable = false)
    private Boolean activaMarcaDefault;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Marca nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFabricante() {
        return this.fabricante;
    }

    public Marca fabricante(String fabricante) {
        this.setFabricante(fabricante);
        return this;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Integer getRegistroInicio() {
        return this.registroInicio;
    }

    public Marca registroInicio(Integer registroInicio) {
        this.setRegistroInicio(registroInicio);
        return this;
    }

    public void setRegistroInicio(Integer registroInicio) {
        this.registroInicio = registroInicio;
    }

    public Integer getRegistroFin() {
        return this.registroFin;
    }

    public Marca registroFin(Integer registroFin) {
        this.setRegistroFin(registroFin);
        return this;
    }

    public void setRegistroFin(Integer registroFin) {
        this.registroFin = registroFin;
    }

    public Integer getRegistroInfoInicio() {
        return this.registroInfoInicio;
    }

    public Marca registroInfoInicio(Integer registroInfoInicio) {
        this.setRegistroInfoInicio(registroInfoInicio);
        return this;
    }

    public void setRegistroInfoInicio(Integer registroInfoInicio) {
        this.registroInfoInicio = registroInfoInicio;
    }

    public Integer getRegistroInfoFin() {
        return this.registroInfoFin;
    }

    public Marca registroInfoFin(Integer registroInfoFin) {
        this.setRegistroInfoFin(registroInfoFin);
        return this;
    }

    public void setRegistroInfoFin(Integer registroInfoFin) {
        this.registroInfoFin = registroInfoFin;
    }

    public Integer getBaudRateDefault() {
        return this.baudRateDefault;
    }

    public Marca baudRateDefault(Integer baudRateDefault) {
        this.setBaudRateDefault(baudRateDefault);
        return this;
    }

    public void setBaudRateDefault(Integer baudRateDefault) {
        this.baudRateDefault = baudRateDefault;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Marca descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivaMarcaDefault() {
        return this.activaMarcaDefault;
    }

    public Marca activaMarcaDefault(Boolean activaMarcaDefault) {
        this.setActivaMarcaDefault(activaMarcaDefault);
        return this;
    }

    public void setActivaMarcaDefault(Boolean activaMarcaDefault) {
        this.activaMarcaDefault = activaMarcaDefault;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marca)) {
            return false;
        }
        return getId() != null && getId().equals(((Marca) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marca{" +
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

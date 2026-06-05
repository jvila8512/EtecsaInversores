package com.etecsa.monitor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ModeloInversor.
 */
@Entity
@Table(name = "modelo_inversor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModeloInversor implements Serializable {

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

    @NotNull
    @Column(name = "capacidad_va", nullable = false)
    private Integer capacidadVa;

    @NotNull
    @Column(name = "voltaje_sistema_nom", nullable = false)
    private Integer voltajeSistemaNom;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Marca marca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ModeloInversor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ModeloInversor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidadVa() {
        return this.capacidadVa;
    }

    public ModeloInversor capacidadVa(Integer capacidadVa) {
        this.setCapacidadVa(capacidadVa);
        return this;
    }

    public void setCapacidadVa(Integer capacidadVa) {
        this.capacidadVa = capacidadVa;
    }

    public Integer getVoltajeSistemaNom() {
        return this.voltajeSistemaNom;
    }

    public ModeloInversor voltajeSistemaNom(Integer voltajeSistemaNom) {
        this.setVoltajeSistemaNom(voltajeSistemaNom);
        return this;
    }

    public void setVoltajeSistemaNom(Integer voltajeSistemaNom) {
        this.voltajeSistemaNom = voltajeSistemaNom;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public ModeloInversor descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public ModeloInversor marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModeloInversor)) {
            return false;
        }
        return getId() != null && getId().equals(((ModeloInversor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModeloInversor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", capacidadVa=" + getCapacidadVa() +
            ", voltajeSistemaNom=" + getVoltajeSistemaNom() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}

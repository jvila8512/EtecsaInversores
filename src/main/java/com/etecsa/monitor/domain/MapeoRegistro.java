package com.etecsa.monitor.domain;

import com.etecsa.monitor.domain.enumeration.TipoDato;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MapeoRegistro — direccion + escala de cada campo por marca.
 * Permite soportar cualquier marca sin cambiar código Java.
 */
@Entity
@Table(name = "mapeo_registro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapeoRegistro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "direccion_registro", nullable = false)
    private Integer direccionRegistro;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_registro", length = 100, nullable = false)
    private String nombreRegistro;

    @NotNull
    @Size(max = 60)
    @Column(name = "campo_sql", length = 60, nullable = false)
    private String campoSql;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato", nullable = false)
    private TipoDato tipoDato;

    @Column(name = "escala")
    private Double escala;

    @Size(max = 10)
    @Column(name = "unidad", length = 10)
    private String unidad;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Marca marca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MapeoRegistro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDireccionRegistro() {
        return this.direccionRegistro;
    }

    public MapeoRegistro direccionRegistro(Integer direccionRegistro) {
        this.setDireccionRegistro(direccionRegistro);
        return this;
    }

    public void setDireccionRegistro(Integer direccionRegistro) {
        this.direccionRegistro = direccionRegistro;
    }

    public String getNombreRegistro() {
        return this.nombreRegistro;
    }

    public MapeoRegistro nombreRegistro(String nombreRegistro) {
        this.setNombreRegistro(nombreRegistro);
        return this;
    }

    public void setNombreRegistro(String nombreRegistro) {
        this.nombreRegistro = nombreRegistro;
    }

    public String getCampoSql() {
        return this.campoSql;
    }

    public MapeoRegistro campoSql(String campoSql) {
        this.setCampoSql(campoSql);
        return this;
    }

    public void setCampoSql(String campoSql) {
        this.campoSql = campoSql;
    }

    public TipoDato getTipoDato() {
        return this.tipoDato;
    }

    public MapeoRegistro tipoDato(TipoDato tipoDato) {
        this.setTipoDato(tipoDato);
        return this;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public Double getEscala() {
        return this.escala;
    }

    public MapeoRegistro escala(Double escala) {
        this.setEscala(escala);
        return this;
    }

    public void setEscala(Double escala) {
        this.escala = escala;
    }

    public String getUnidad() {
        return this.unidad;
    }

    public MapeoRegistro unidad(String unidad) {
        this.setUnidad(unidad);
        return this;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public MapeoRegistro activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public MapeoRegistro descripcion(String descripcion) {
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

    public MapeoRegistro marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapeoRegistro)) {
            return false;
        }
        return getId() != null && getId().equals(((MapeoRegistro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapeoRegistro{" +
            "id=" + getId() +
            ", direccionRegistro=" + getDireccionRegistro() +
            ", nombreRegistro='" + getNombreRegistro() + "'" +
            ", campoSql='" + getCampoSql() + "'" +
            ", tipoDato='" + getTipoDato() + "'" +
            ", escala=" + getEscala() +
            ", unidad='" + getUnidad() + "'" +
            ", activo='" + getActivo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}

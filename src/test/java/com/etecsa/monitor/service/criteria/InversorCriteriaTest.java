package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InversorCriteriaTest {

    @Test
    void newInversorCriteriaHasAllFiltersNullTest() {
        var inversorCriteria = new InversorCriteria();
        assertThat(inversorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inversorCriteriaFluentMethodsCreatesFiltersTest() {
        var inversorCriteria = new InversorCriteria();

        setAllFilters(inversorCriteria);

        assertThat(inversorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inversorCriteriaCopyCreatesNullFilterTest() {
        var inversorCriteria = new InversorCriteria();
        var copy = inversorCriteria.copy();

        assertThat(inversorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inversorCriteria)
        );
    }

    @Test
    void inversorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inversorCriteria = new InversorCriteria();
        setAllFilters(inversorCriteria);

        var copy = inversorCriteria.copy();

        assertThat(inversorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inversorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inversorCriteria = new InversorCriteria();

        assertThat(inversorCriteria).hasToString("InversorCriteria{}");
    }

    private static void setAllFilters(InversorCriteria inversorCriteria) {
        inversorCriteria.id();
        inversorCriteria.nombre();
        inversorCriteria.numeroSerie();
        inversorCriteria.esMaster();
        inversorCriteria.tipoConexion();
        inversorCriteria.puertoSerial();
        inversorCriteria.slaveId();
        inversorCriteria.activo();
        inversorCriteria.estadoDispositivo();
        inversorCriteria.ultimaLectura();
        inversorCriteria.ultimoError();
        inversorCriteria.periodoMuestreoSeg();
        inversorCriteria.fechaRegistro();
        inversorCriteria.ultimaConexion();
        inversorCriteria.sitioId();
        inversorCriteria.marcaId();
        inversorCriteria.modeloId();
        inversorCriteria.gatewayId();
        inversorCriteria.masterReferenciaId();
        inversorCriteria.distinct();
    }

    private static Condition<InversorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getNumeroSerie()) &&
                condition.apply(criteria.getEsMaster()) &&
                condition.apply(criteria.getTipoConexion()) &&
                condition.apply(criteria.getPuertoSerial()) &&
                condition.apply(criteria.getSlaveId()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getEstadoDispositivo()) &&
                condition.apply(criteria.getUltimaLectura()) &&
                condition.apply(criteria.getUltimoError()) &&
                condition.apply(criteria.getPeriodoMuestreoSeg()) &&
                condition.apply(criteria.getFechaRegistro()) &&
                condition.apply(criteria.getUltimaConexion()) &&
                condition.apply(criteria.getSitioId()) &&
                condition.apply(criteria.getMarcaId()) &&
                condition.apply(criteria.getModeloId()) &&
                condition.apply(criteria.getGatewayId()) &&
                condition.apply(criteria.getMasterReferenciaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InversorCriteria> copyFiltersAre(InversorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getNumeroSerie(), copy.getNumeroSerie()) &&
                condition.apply(criteria.getEsMaster(), copy.getEsMaster()) &&
                condition.apply(criteria.getTipoConexion(), copy.getTipoConexion()) &&
                condition.apply(criteria.getPuertoSerial(), copy.getPuertoSerial()) &&
                condition.apply(criteria.getSlaveId(), copy.getSlaveId()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getEstadoDispositivo(), copy.getEstadoDispositivo()) &&
                condition.apply(criteria.getUltimaLectura(), copy.getUltimaLectura()) &&
                condition.apply(criteria.getUltimoError(), copy.getUltimoError()) &&
                condition.apply(criteria.getPeriodoMuestreoSeg(), copy.getPeriodoMuestreoSeg()) &&
                condition.apply(criteria.getFechaRegistro(), copy.getFechaRegistro()) &&
                condition.apply(criteria.getUltimaConexion(), copy.getUltimaConexion()) &&
                condition.apply(criteria.getSitioId(), copy.getSitioId()) &&
                condition.apply(criteria.getMarcaId(), copy.getMarcaId()) &&
                condition.apply(criteria.getModeloId(), copy.getModeloId()) &&
                condition.apply(criteria.getGatewayId(), copy.getGatewayId()) &&
                condition.apply(criteria.getMasterReferenciaId(), copy.getMasterReferenciaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

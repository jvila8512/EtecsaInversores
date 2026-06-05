package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DatoInversorCriteriaTest {

    @Test
    void newDatoInversorCriteriaHasAllFiltersNullTest() {
        var datoInversorCriteria = new DatoInversorCriteria();
        assertThat(datoInversorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void datoInversorCriteriaFluentMethodsCreatesFiltersTest() {
        var datoInversorCriteria = new DatoInversorCriteria();

        setAllFilters(datoInversorCriteria);

        assertThat(datoInversorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void datoInversorCriteriaCopyCreatesNullFilterTest() {
        var datoInversorCriteria = new DatoInversorCriteria();
        var copy = datoInversorCriteria.copy();

        assertThat(datoInversorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(datoInversorCriteria)
        );
    }

    @Test
    void datoInversorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var datoInversorCriteria = new DatoInversorCriteria();
        setAllFilters(datoInversorCriteria);

        var copy = datoInversorCriteria.copy();

        assertThat(datoInversorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(datoInversorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var datoInversorCriteria = new DatoInversorCriteria();

        assertThat(datoInversorCriteria).hasToString("DatoInversorCriteria{}");
    }

    private static void setAllFilters(DatoInversorCriteria datoInversorCriteria) {
        datoInversorCriteria.id();
        datoInversorCriteria.tiempo();
        datoInversorCriteria.bateriaVoltajeV();
        datoInversorCriteria.bateriaCorrienteA();
        datoInversorCriteria.bateriaPotenciaW();
        datoInversorCriteria.pvVoltajeV();
        datoInversorCriteria.pvPotenciaW();
        datoInversorCriteria.salidaVoltajeV();
        datoInversorCriteria.salidaPotenciaW();
        datoInversorCriteria.salidaAparenteVa();
        datoInversorCriteria.cargaPorcentaje();
        datoInversorCriteria.redVoltajeV();
        datoInversorCriteria.redFrecuenciaHz();
        datoInversorCriteria.modoOperacion();
        datoInversorCriteria.codigoFallo();
        datoInversorCriteria.eficienciaPct();
        datoInversorCriteria.consumoRedW();
        datoInversorCriteria.bateriaCargando();
        datoInversorCriteria.pvActivo();
        datoInversorCriteria.redActiva();
        datoInversorCriteria.crcValido();
        datoInversorCriteria.latenciaMs();
        datoInversorCriteria.inversorId();
        datoInversorCriteria.distinct();
    }

    private static Condition<DatoInversorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTiempo()) &&
                condition.apply(criteria.getBateriaVoltajeV()) &&
                condition.apply(criteria.getBateriaCorrienteA()) &&
                condition.apply(criteria.getBateriaPotenciaW()) &&
                condition.apply(criteria.getPvVoltajeV()) &&
                condition.apply(criteria.getPvPotenciaW()) &&
                condition.apply(criteria.getSalidaVoltajeV()) &&
                condition.apply(criteria.getSalidaPotenciaW()) &&
                condition.apply(criteria.getSalidaAparenteVa()) &&
                condition.apply(criteria.getCargaPorcentaje()) &&
                condition.apply(criteria.getRedVoltajeV()) &&
                condition.apply(criteria.getRedFrecuenciaHz()) &&
                condition.apply(criteria.getModoOperacion()) &&
                condition.apply(criteria.getCodigoFallo()) &&
                condition.apply(criteria.getEficienciaPct()) &&
                condition.apply(criteria.getConsumoRedW()) &&
                condition.apply(criteria.getBateriaCargando()) &&
                condition.apply(criteria.getPvActivo()) &&
                condition.apply(criteria.getRedActiva()) &&
                condition.apply(criteria.getCrcValido()) &&
                condition.apply(criteria.getLatenciaMs()) &&
                condition.apply(criteria.getInversorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DatoInversorCriteria> copyFiltersAre(
        DatoInversorCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTiempo(), copy.getTiempo()) &&
                condition.apply(criteria.getBateriaVoltajeV(), copy.getBateriaVoltajeV()) &&
                condition.apply(criteria.getBateriaCorrienteA(), copy.getBateriaCorrienteA()) &&
                condition.apply(criteria.getBateriaPotenciaW(), copy.getBateriaPotenciaW()) &&
                condition.apply(criteria.getPvVoltajeV(), copy.getPvVoltajeV()) &&
                condition.apply(criteria.getPvPotenciaW(), copy.getPvPotenciaW()) &&
                condition.apply(criteria.getSalidaVoltajeV(), copy.getSalidaVoltajeV()) &&
                condition.apply(criteria.getSalidaPotenciaW(), copy.getSalidaPotenciaW()) &&
                condition.apply(criteria.getSalidaAparenteVa(), copy.getSalidaAparenteVa()) &&
                condition.apply(criteria.getCargaPorcentaje(), copy.getCargaPorcentaje()) &&
                condition.apply(criteria.getRedVoltajeV(), copy.getRedVoltajeV()) &&
                condition.apply(criteria.getRedFrecuenciaHz(), copy.getRedFrecuenciaHz()) &&
                condition.apply(criteria.getModoOperacion(), copy.getModoOperacion()) &&
                condition.apply(criteria.getCodigoFallo(), copy.getCodigoFallo()) &&
                condition.apply(criteria.getEficienciaPct(), copy.getEficienciaPct()) &&
                condition.apply(criteria.getConsumoRedW(), copy.getConsumoRedW()) &&
                condition.apply(criteria.getBateriaCargando(), copy.getBateriaCargando()) &&
                condition.apply(criteria.getPvActivo(), copy.getPvActivo()) &&
                condition.apply(criteria.getRedActiva(), copy.getRedActiva()) &&
                condition.apply(criteria.getCrcValido(), copy.getCrcValido()) &&
                condition.apply(criteria.getLatenciaMs(), copy.getLatenciaMs()) &&
                condition.apply(criteria.getInversorId(), copy.getInversorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

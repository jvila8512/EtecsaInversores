package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DatoEficienciaCriteriaTest {

    @Test
    void newDatoEficienciaCriteriaHasAllFiltersNullTest() {
        var datoEficienciaCriteria = new DatoEficienciaCriteria();
        assertThat(datoEficienciaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void datoEficienciaCriteriaFluentMethodsCreatesFiltersTest() {
        var datoEficienciaCriteria = new DatoEficienciaCriteria();

        setAllFilters(datoEficienciaCriteria);

        assertThat(datoEficienciaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void datoEficienciaCriteriaCopyCreatesNullFilterTest() {
        var datoEficienciaCriteria = new DatoEficienciaCriteria();
        var copy = datoEficienciaCriteria.copy();

        assertThat(datoEficienciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(datoEficienciaCriteria)
        );
    }

    @Test
    void datoEficienciaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var datoEficienciaCriteria = new DatoEficienciaCriteria();
        setAllFilters(datoEficienciaCriteria);

        var copy = datoEficienciaCriteria.copy();

        assertThat(datoEficienciaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(datoEficienciaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var datoEficienciaCriteria = new DatoEficienciaCriteria();

        assertThat(datoEficienciaCriteria).hasToString("DatoEficienciaCriteria{}");
    }

    private static void setAllFilters(DatoEficienciaCriteria datoEficienciaCriteria) {
        datoEficienciaCriteria.id();
        datoEficienciaCriteria.fecha();
        datoEficienciaCriteria.energiaPvWh();
        datoEficienciaCriteria.energiaCargaWh();
        datoEficienciaCriteria.energiaRedWh();
        datoEficienciaCriteria.eficienciaPromPct();
        datoEficienciaCriteria.horasSol();
        datoEficienciaCriteria.picoPvW();
        datoEficienciaCriteria.picoCargaW();
        datoEficienciaCriteria.maxBateriaV();
        datoEficienciaCriteria.minBateriaV();
        datoEficienciaCriteria.totalLecturas();
        datoEficienciaCriteria.calculadoEn();
        datoEficienciaCriteria.inversorId();
        datoEficienciaCriteria.distinct();
    }

    private static Condition<DatoEficienciaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getEnergiaPvWh()) &&
                condition.apply(criteria.getEnergiaCargaWh()) &&
                condition.apply(criteria.getEnergiaRedWh()) &&
                condition.apply(criteria.getEficienciaPromPct()) &&
                condition.apply(criteria.getHorasSol()) &&
                condition.apply(criteria.getPicoPvW()) &&
                condition.apply(criteria.getPicoCargaW()) &&
                condition.apply(criteria.getMaxBateriaV()) &&
                condition.apply(criteria.getMinBateriaV()) &&
                condition.apply(criteria.getTotalLecturas()) &&
                condition.apply(criteria.getCalculadoEn()) &&
                condition.apply(criteria.getInversorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DatoEficienciaCriteria> copyFiltersAre(
        DatoEficienciaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getEnergiaPvWh(), copy.getEnergiaPvWh()) &&
                condition.apply(criteria.getEnergiaCargaWh(), copy.getEnergiaCargaWh()) &&
                condition.apply(criteria.getEnergiaRedWh(), copy.getEnergiaRedWh()) &&
                condition.apply(criteria.getEficienciaPromPct(), copy.getEficienciaPromPct()) &&
                condition.apply(criteria.getHorasSol(), copy.getHorasSol()) &&
                condition.apply(criteria.getPicoPvW(), copy.getPicoPvW()) &&
                condition.apply(criteria.getPicoCargaW(), copy.getPicoCargaW()) &&
                condition.apply(criteria.getMaxBateriaV(), copy.getMaxBateriaV()) &&
                condition.apply(criteria.getMinBateriaV(), copy.getMinBateriaV()) &&
                condition.apply(criteria.getTotalLecturas(), copy.getTotalLecturas()) &&
                condition.apply(criteria.getCalculadoEn(), copy.getCalculadoEn()) &&
                condition.apply(criteria.getInversorId(), copy.getInversorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

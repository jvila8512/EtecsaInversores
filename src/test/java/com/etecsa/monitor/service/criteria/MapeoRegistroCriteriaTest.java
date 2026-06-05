package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MapeoRegistroCriteriaTest {

    @Test
    void newMapeoRegistroCriteriaHasAllFiltersNullTest() {
        var mapeoRegistroCriteria = new MapeoRegistroCriteria();
        assertThat(mapeoRegistroCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mapeoRegistroCriteriaFluentMethodsCreatesFiltersTest() {
        var mapeoRegistroCriteria = new MapeoRegistroCriteria();

        setAllFilters(mapeoRegistroCriteria);

        assertThat(mapeoRegistroCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mapeoRegistroCriteriaCopyCreatesNullFilterTest() {
        var mapeoRegistroCriteria = new MapeoRegistroCriteria();
        var copy = mapeoRegistroCriteria.copy();

        assertThat(mapeoRegistroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mapeoRegistroCriteria)
        );
    }

    @Test
    void mapeoRegistroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mapeoRegistroCriteria = new MapeoRegistroCriteria();
        setAllFilters(mapeoRegistroCriteria);

        var copy = mapeoRegistroCriteria.copy();

        assertThat(mapeoRegistroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mapeoRegistroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mapeoRegistroCriteria = new MapeoRegistroCriteria();

        assertThat(mapeoRegistroCriteria).hasToString("MapeoRegistroCriteria{}");
    }

    private static void setAllFilters(MapeoRegistroCriteria mapeoRegistroCriteria) {
        mapeoRegistroCriteria.id();
        mapeoRegistroCriteria.direccionRegistro();
        mapeoRegistroCriteria.nombreRegistro();
        mapeoRegistroCriteria.campoSql();
        mapeoRegistroCriteria.tipoDato();
        mapeoRegistroCriteria.escala();
        mapeoRegistroCriteria.unidad();
        mapeoRegistroCriteria.activo();
        mapeoRegistroCriteria.descripcion();
        mapeoRegistroCriteria.marcaId();
        mapeoRegistroCriteria.distinct();
    }

    private static Condition<MapeoRegistroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDireccionRegistro()) &&
                condition.apply(criteria.getNombreRegistro()) &&
                condition.apply(criteria.getCampoSql()) &&
                condition.apply(criteria.getTipoDato()) &&
                condition.apply(criteria.getEscala()) &&
                condition.apply(criteria.getUnidad()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getMarcaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MapeoRegistroCriteria> copyFiltersAre(
        MapeoRegistroCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDireccionRegistro(), copy.getDireccionRegistro()) &&
                condition.apply(criteria.getNombreRegistro(), copy.getNombreRegistro()) &&
                condition.apply(criteria.getCampoSql(), copy.getCampoSql()) &&
                condition.apply(criteria.getTipoDato(), copy.getTipoDato()) &&
                condition.apply(criteria.getEscala(), copy.getEscala()) &&
                condition.apply(criteria.getUnidad(), copy.getUnidad()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getMarcaId(), copy.getMarcaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GatewayTcpCriteriaTest {

    @Test
    void newGatewayTcpCriteriaHasAllFiltersNullTest() {
        var gatewayTcpCriteria = new GatewayTcpCriteria();
        assertThat(gatewayTcpCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void gatewayTcpCriteriaFluentMethodsCreatesFiltersTest() {
        var gatewayTcpCriteria = new GatewayTcpCriteria();

        setAllFilters(gatewayTcpCriteria);

        assertThat(gatewayTcpCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void gatewayTcpCriteriaCopyCreatesNullFilterTest() {
        var gatewayTcpCriteria = new GatewayTcpCriteria();
        var copy = gatewayTcpCriteria.copy();

        assertThat(gatewayTcpCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(gatewayTcpCriteria)
        );
    }

    @Test
    void gatewayTcpCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var gatewayTcpCriteria = new GatewayTcpCriteria();
        setAllFilters(gatewayTcpCriteria);

        var copy = gatewayTcpCriteria.copy();

        assertThat(gatewayTcpCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(gatewayTcpCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var gatewayTcpCriteria = new GatewayTcpCriteria();

        assertThat(gatewayTcpCriteria).hasToString("GatewayTcpCriteria{}");
    }

    private static void setAllFilters(GatewayTcpCriteria gatewayTcpCriteria) {
        gatewayTcpCriteria.id();
        gatewayTcpCriteria.nombre();
        gatewayTcpCriteria.descripcion();
        gatewayTcpCriteria.ubicacion();
        gatewayTcpCriteria.direccionIp();
        gatewayTcpCriteria.puerto();
        gatewayTcpCriteria.activo();
        gatewayTcpCriteria.estadoConexion();
        gatewayTcpCriteria.ultimaLectura();
        gatewayTcpCriteria.ultimoError();
        gatewayTcpCriteria.tiempoEsperaMs();
        gatewayTcpCriteria.reintentos();
        gatewayTcpCriteria.sitioId();
        gatewayTcpCriteria.distinct();
    }

    private static Condition<GatewayTcpCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getUbicacion()) &&
                condition.apply(criteria.getDireccionIp()) &&
                condition.apply(criteria.getPuerto()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getEstadoConexion()) &&
                condition.apply(criteria.getUltimaLectura()) &&
                condition.apply(criteria.getUltimoError()) &&
                condition.apply(criteria.getTiempoEsperaMs()) &&
                condition.apply(criteria.getReintentos()) &&
                condition.apply(criteria.getSitioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GatewayTcpCriteria> copyFiltersAre(GatewayTcpCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getUbicacion(), copy.getUbicacion()) &&
                condition.apply(criteria.getDireccionIp(), copy.getDireccionIp()) &&
                condition.apply(criteria.getPuerto(), copy.getPuerto()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getEstadoConexion(), copy.getEstadoConexion()) &&
                condition.apply(criteria.getUltimaLectura(), copy.getUltimaLectura()) &&
                condition.apply(criteria.getUltimoError(), copy.getUltimoError()) &&
                condition.apply(criteria.getTiempoEsperaMs(), copy.getTiempoEsperaMs()) &&
                condition.apply(criteria.getReintentos(), copy.getReintentos()) &&
                condition.apply(criteria.getSitioId(), copy.getSitioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

package com.etecsa.monitor.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventoAlertaCriteriaTest {

    @Test
    void newEventoAlertaCriteriaHasAllFiltersNullTest() {
        var eventoAlertaCriteria = new EventoAlertaCriteria();
        assertThat(eventoAlertaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void eventoAlertaCriteriaFluentMethodsCreatesFiltersTest() {
        var eventoAlertaCriteria = new EventoAlertaCriteria();

        setAllFilters(eventoAlertaCriteria);

        assertThat(eventoAlertaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void eventoAlertaCriteriaCopyCreatesNullFilterTest() {
        var eventoAlertaCriteria = new EventoAlertaCriteria();
        var copy = eventoAlertaCriteria.copy();

        assertThat(eventoAlertaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(eventoAlertaCriteria)
        );
    }

    @Test
    void eventoAlertaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventoAlertaCriteria = new EventoAlertaCriteria();
        setAllFilters(eventoAlertaCriteria);

        var copy = eventoAlertaCriteria.copy();

        assertThat(eventoAlertaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(eventoAlertaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventoAlertaCriteria = new EventoAlertaCriteria();

        assertThat(eventoAlertaCriteria).hasToString("EventoAlertaCriteria{}");
    }

    private static void setAllFilters(EventoAlertaCriteria eventoAlertaCriteria) {
        eventoAlertaCriteria.id();
        eventoAlertaCriteria.timestamp();
        eventoAlertaCriteria.nombreAlerta();
        eventoAlertaCriteria.mensaje();
        eventoAlertaCriteria.campoMonitorizado();
        eventoAlertaCriteria.valorDetectado();
        eventoAlertaCriteria.umbral();
        eventoAlertaCriteria.operador();
        eventoAlertaCriteria.nivelSeveridad();
        eventoAlertaCriteria.notificacionEnviada();
        eventoAlertaCriteria.canalUsado();
        eventoAlertaCriteria.resuelto();
        eventoAlertaCriteria.fechaResolucion();
        eventoAlertaCriteria.notas();
        eventoAlertaCriteria.inversorId();
        eventoAlertaCriteria.alertaConfigId();
        eventoAlertaCriteria.distinct();
    }

    private static Condition<EventoAlertaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getNombreAlerta()) &&
                condition.apply(criteria.getMensaje()) &&
                condition.apply(criteria.getCampoMonitorizado()) &&
                condition.apply(criteria.getValorDetectado()) &&
                condition.apply(criteria.getUmbral()) &&
                condition.apply(criteria.getOperador()) &&
                condition.apply(criteria.getNivelSeveridad()) &&
                condition.apply(criteria.getNotificacionEnviada()) &&
                condition.apply(criteria.getCanalUsado()) &&
                condition.apply(criteria.getResuelto()) &&
                condition.apply(criteria.getFechaResolucion()) &&
                condition.apply(criteria.getNotas()) &&
                condition.apply(criteria.getInversorId()) &&
                condition.apply(criteria.getAlertaConfigId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventoAlertaCriteria> copyFiltersAre(
        EventoAlertaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getNombreAlerta(), copy.getNombreAlerta()) &&
                condition.apply(criteria.getMensaje(), copy.getMensaje()) &&
                condition.apply(criteria.getCampoMonitorizado(), copy.getCampoMonitorizado()) &&
                condition.apply(criteria.getValorDetectado(), copy.getValorDetectado()) &&
                condition.apply(criteria.getUmbral(), copy.getUmbral()) &&
                condition.apply(criteria.getOperador(), copy.getOperador()) &&
                condition.apply(criteria.getNivelSeveridad(), copy.getNivelSeveridad()) &&
                condition.apply(criteria.getNotificacionEnviada(), copy.getNotificacionEnviada()) &&
                condition.apply(criteria.getCanalUsado(), copy.getCanalUsado()) &&
                condition.apply(criteria.getResuelto(), copy.getResuelto()) &&
                condition.apply(criteria.getFechaResolucion(), copy.getFechaResolucion()) &&
                condition.apply(criteria.getNotas(), copy.getNotas()) &&
                condition.apply(criteria.getInversorId(), copy.getInversorId()) &&
                condition.apply(criteria.getAlertaConfigId(), copy.getAlertaConfigId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

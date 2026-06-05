package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.EventoAlertaAsserts.*;
import static com.etecsa.monitor.domain.EventoAlertaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventoAlertaMapperTest {

    private EventoAlertaMapper eventoAlertaMapper;

    @BeforeEach
    void setUp() {
        eventoAlertaMapper = new EventoAlertaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventoAlertaSample1();
        var actual = eventoAlertaMapper.toEntity(eventoAlertaMapper.toDto(expected));
        assertEventoAlertaAllPropertiesEquals(expected, actual);
    }
}

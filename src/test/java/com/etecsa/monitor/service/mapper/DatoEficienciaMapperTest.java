package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.DatoEficienciaAsserts.*;
import static com.etecsa.monitor.domain.DatoEficienciaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatoEficienciaMapperTest {

    private DatoEficienciaMapper datoEficienciaMapper;

    @BeforeEach
    void setUp() {
        datoEficienciaMapper = new DatoEficienciaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDatoEficienciaSample1();
        var actual = datoEficienciaMapper.toEntity(datoEficienciaMapper.toDto(expected));
        assertDatoEficienciaAllPropertiesEquals(expected, actual);
    }
}

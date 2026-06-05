package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.DatoInversorAsserts.*;
import static com.etecsa.monitor.domain.DatoInversorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatoInversorMapperTest {

    private DatoInversorMapper datoInversorMapper;

    @BeforeEach
    void setUp() {
        datoInversorMapper = new DatoInversorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDatoInversorSample1();
        var actual = datoInversorMapper.toEntity(datoInversorMapper.toDto(expected));
        assertDatoInversorAllPropertiesEquals(expected, actual);
    }
}

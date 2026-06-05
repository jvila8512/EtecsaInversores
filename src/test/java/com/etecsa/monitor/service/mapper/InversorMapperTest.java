package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.InversorAsserts.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InversorMapperTest {

    private InversorMapper inversorMapper;

    @BeforeEach
    void setUp() {
        inversorMapper = new InversorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInversorSample1();
        var actual = inversorMapper.toEntity(inversorMapper.toDto(expected));
        assertInversorAllPropertiesEquals(expected, actual);
    }
}

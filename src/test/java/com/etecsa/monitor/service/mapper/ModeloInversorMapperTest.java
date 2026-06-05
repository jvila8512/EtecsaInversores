package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.ModeloInversorAsserts.*;
import static com.etecsa.monitor.domain.ModeloInversorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModeloInversorMapperTest {

    private ModeloInversorMapper modeloInversorMapper;

    @BeforeEach
    void setUp() {
        modeloInversorMapper = new ModeloInversorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModeloInversorSample1();
        var actual = modeloInversorMapper.toEntity(modeloInversorMapper.toDto(expected));
        assertModeloInversorAllPropertiesEquals(expected, actual);
    }
}

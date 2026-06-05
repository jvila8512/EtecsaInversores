package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.MarcaAsserts.*;
import static com.etecsa.monitor.domain.MarcaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarcaMapperTest {

    private MarcaMapper marcaMapper;

    @BeforeEach
    void setUp() {
        marcaMapper = new MarcaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMarcaSample1();
        var actual = marcaMapper.toEntity(marcaMapper.toDto(expected));
        assertMarcaAllPropertiesEquals(expected, actual);
    }
}

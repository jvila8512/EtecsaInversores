package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.MapeoRegistroAsserts.*;
import static com.etecsa.monitor.domain.MapeoRegistroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapeoRegistroMapperTest {

    private MapeoRegistroMapper mapeoRegistroMapper;

    @BeforeEach
    void setUp() {
        mapeoRegistroMapper = new MapeoRegistroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMapeoRegistroSample1();
        var actual = mapeoRegistroMapper.toEntity(mapeoRegistroMapper.toDto(expected));
        assertMapeoRegistroAllPropertiesEquals(expected, actual);
    }
}

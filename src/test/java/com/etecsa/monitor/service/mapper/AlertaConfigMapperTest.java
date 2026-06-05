package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.AlertaConfigAsserts.*;
import static com.etecsa.monitor.domain.AlertaConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlertaConfigMapperTest {

    private AlertaConfigMapper alertaConfigMapper;

    @BeforeEach
    void setUp() {
        alertaConfigMapper = new AlertaConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlertaConfigSample1();
        var actual = alertaConfigMapper.toEntity(alertaConfigMapper.toDto(expected));
        assertAlertaConfigAllPropertiesEquals(expected, actual);
    }
}

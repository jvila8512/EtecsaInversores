package com.etecsa.monitor.service.mapper;

import static com.etecsa.monitor.domain.GatewayTcpAsserts.*;
import static com.etecsa.monitor.domain.GatewayTcpTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GatewayTcpMapperTest {

    private GatewayTcpMapper gatewayTcpMapper;

    @BeforeEach
    void setUp() {
        gatewayTcpMapper = new GatewayTcpMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGatewayTcpSample1();
        var actual = gatewayTcpMapper.toEntity(gatewayTcpMapper.toDto(expected));
        assertGatewayTcpAllPropertiesEquals(expected, actual);
    }
}

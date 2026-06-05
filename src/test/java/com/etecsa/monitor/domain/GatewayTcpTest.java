package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.GatewayTcpTestSamples.*;
import static com.etecsa.monitor.domain.SitioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GatewayTcpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GatewayTcp.class);
        GatewayTcp gatewayTcp1 = getGatewayTcpSample1();
        GatewayTcp gatewayTcp2 = new GatewayTcp();
        assertThat(gatewayTcp1).isNotEqualTo(gatewayTcp2);

        gatewayTcp2.setId(gatewayTcp1.getId());
        assertThat(gatewayTcp1).isEqualTo(gatewayTcp2);

        gatewayTcp2 = getGatewayTcpSample2();
        assertThat(gatewayTcp1).isNotEqualTo(gatewayTcp2);
    }

    @Test
    void sitioTest() {
        GatewayTcp gatewayTcp = getGatewayTcpRandomSampleGenerator();
        Sitio sitioBack = getSitioRandomSampleGenerator();

        gatewayTcp.setSitio(sitioBack);
        assertThat(gatewayTcp.getSitio()).isEqualTo(sitioBack);

        gatewayTcp.sitio(null);
        assertThat(gatewayTcp.getSitio()).isNull();
    }
}

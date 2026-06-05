package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GatewayTcpDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GatewayTcpDTO.class);
        GatewayTcpDTO gatewayTcpDTO1 = new GatewayTcpDTO();
        gatewayTcpDTO1.setId(1L);
        GatewayTcpDTO gatewayTcpDTO2 = new GatewayTcpDTO();
        assertThat(gatewayTcpDTO1).isNotEqualTo(gatewayTcpDTO2);
        gatewayTcpDTO2.setId(gatewayTcpDTO1.getId());
        assertThat(gatewayTcpDTO1).isEqualTo(gatewayTcpDTO2);
        gatewayTcpDTO2.setId(2L);
        assertThat(gatewayTcpDTO1).isNotEqualTo(gatewayTcpDTO2);
        gatewayTcpDTO1.setId(null);
        assertThat(gatewayTcpDTO1).isNotEqualTo(gatewayTcpDTO2);
    }
}

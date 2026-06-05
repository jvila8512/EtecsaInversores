package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertaConfigDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertaConfigDTO.class);
        AlertaConfigDTO alertaConfigDTO1 = new AlertaConfigDTO();
        alertaConfigDTO1.setId(1L);
        AlertaConfigDTO alertaConfigDTO2 = new AlertaConfigDTO();
        assertThat(alertaConfigDTO1).isNotEqualTo(alertaConfigDTO2);
        alertaConfigDTO2.setId(alertaConfigDTO1.getId());
        assertThat(alertaConfigDTO1).isEqualTo(alertaConfigDTO2);
        alertaConfigDTO2.setId(2L);
        assertThat(alertaConfigDTO1).isNotEqualTo(alertaConfigDTO2);
        alertaConfigDTO1.setId(null);
        assertThat(alertaConfigDTO1).isNotEqualTo(alertaConfigDTO2);
    }
}

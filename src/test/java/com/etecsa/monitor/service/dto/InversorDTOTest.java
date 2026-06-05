package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InversorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InversorDTO.class);
        InversorDTO inversorDTO1 = new InversorDTO();
        inversorDTO1.setId(1L);
        InversorDTO inversorDTO2 = new InversorDTO();
        assertThat(inversorDTO1).isNotEqualTo(inversorDTO2);
        inversorDTO2.setId(inversorDTO1.getId());
        assertThat(inversorDTO1).isEqualTo(inversorDTO2);
        inversorDTO2.setId(2L);
        assertThat(inversorDTO1).isNotEqualTo(inversorDTO2);
        inversorDTO1.setId(null);
        assertThat(inversorDTO1).isNotEqualTo(inversorDTO2);
    }
}

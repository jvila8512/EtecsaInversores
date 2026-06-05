package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatoInversorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoInversorDTO.class);
        DatoInversorDTO datoInversorDTO1 = new DatoInversorDTO();
        datoInversorDTO1.setId(1L);
        DatoInversorDTO datoInversorDTO2 = new DatoInversorDTO();
        assertThat(datoInversorDTO1).isNotEqualTo(datoInversorDTO2);
        datoInversorDTO2.setId(datoInversorDTO1.getId());
        assertThat(datoInversorDTO1).isEqualTo(datoInversorDTO2);
        datoInversorDTO2.setId(2L);
        assertThat(datoInversorDTO1).isNotEqualTo(datoInversorDTO2);
        datoInversorDTO1.setId(null);
        assertThat(datoInversorDTO1).isNotEqualTo(datoInversorDTO2);
    }
}

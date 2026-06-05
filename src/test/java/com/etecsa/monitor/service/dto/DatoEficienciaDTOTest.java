package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatoEficienciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoEficienciaDTO.class);
        DatoEficienciaDTO datoEficienciaDTO1 = new DatoEficienciaDTO();
        datoEficienciaDTO1.setId(1L);
        DatoEficienciaDTO datoEficienciaDTO2 = new DatoEficienciaDTO();
        assertThat(datoEficienciaDTO1).isNotEqualTo(datoEficienciaDTO2);
        datoEficienciaDTO2.setId(datoEficienciaDTO1.getId());
        assertThat(datoEficienciaDTO1).isEqualTo(datoEficienciaDTO2);
        datoEficienciaDTO2.setId(2L);
        assertThat(datoEficienciaDTO1).isNotEqualTo(datoEficienciaDTO2);
        datoEficienciaDTO1.setId(null);
        assertThat(datoEficienciaDTO1).isNotEqualTo(datoEficienciaDTO2);
    }
}

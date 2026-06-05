package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModeloInversorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeloInversorDTO.class);
        ModeloInversorDTO modeloInversorDTO1 = new ModeloInversorDTO();
        modeloInversorDTO1.setId(1L);
        ModeloInversorDTO modeloInversorDTO2 = new ModeloInversorDTO();
        assertThat(modeloInversorDTO1).isNotEqualTo(modeloInversorDTO2);
        modeloInversorDTO2.setId(modeloInversorDTO1.getId());
        assertThat(modeloInversorDTO1).isEqualTo(modeloInversorDTO2);
        modeloInversorDTO2.setId(2L);
        assertThat(modeloInversorDTO1).isNotEqualTo(modeloInversorDTO2);
        modeloInversorDTO1.setId(null);
        assertThat(modeloInversorDTO1).isNotEqualTo(modeloInversorDTO2);
    }
}

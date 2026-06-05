package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.DatoInversorTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatoInversorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoInversor.class);
        DatoInversor datoInversor1 = getDatoInversorSample1();
        DatoInversor datoInversor2 = new DatoInversor();
        assertThat(datoInversor1).isNotEqualTo(datoInversor2);

        datoInversor2.setId(datoInversor1.getId());
        assertThat(datoInversor1).isEqualTo(datoInversor2);

        datoInversor2 = getDatoInversorSample2();
        assertThat(datoInversor1).isNotEqualTo(datoInversor2);
    }

    @Test
    void inversorTest() {
        DatoInversor datoInversor = getDatoInversorRandomSampleGenerator();
        Inversor inversorBack = getInversorRandomSampleGenerator();

        datoInversor.setInversor(inversorBack);
        assertThat(datoInversor.getInversor()).isEqualTo(inversorBack);

        datoInversor.inversor(null);
        assertThat(datoInversor.getInversor()).isNull();
    }
}

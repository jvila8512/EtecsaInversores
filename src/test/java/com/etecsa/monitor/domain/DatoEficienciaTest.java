package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.DatoEficienciaTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DatoEficienciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoEficiencia.class);
        DatoEficiencia datoEficiencia1 = getDatoEficienciaSample1();
        DatoEficiencia datoEficiencia2 = new DatoEficiencia();
        assertThat(datoEficiencia1).isNotEqualTo(datoEficiencia2);

        datoEficiencia2.setId(datoEficiencia1.getId());
        assertThat(datoEficiencia1).isEqualTo(datoEficiencia2);

        datoEficiencia2 = getDatoEficienciaSample2();
        assertThat(datoEficiencia1).isNotEqualTo(datoEficiencia2);
    }

    @Test
    void inversorTest() {
        DatoEficiencia datoEficiencia = getDatoEficienciaRandomSampleGenerator();
        Inversor inversorBack = getInversorRandomSampleGenerator();

        datoEficiencia.setInversor(inversorBack);
        assertThat(datoEficiencia.getInversor()).isEqualTo(inversorBack);

        datoEficiencia.inversor(null);
        assertThat(datoEficiencia.getInversor()).isNull();
    }
}

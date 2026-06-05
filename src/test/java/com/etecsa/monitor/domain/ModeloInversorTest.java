package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.MarcaTestSamples.*;
import static com.etecsa.monitor.domain.ModeloInversorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModeloInversorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeloInversor.class);
        ModeloInversor modeloInversor1 = getModeloInversorSample1();
        ModeloInversor modeloInversor2 = new ModeloInversor();
        assertThat(modeloInversor1).isNotEqualTo(modeloInversor2);

        modeloInversor2.setId(modeloInversor1.getId());
        assertThat(modeloInversor1).isEqualTo(modeloInversor2);

        modeloInversor2 = getModeloInversorSample2();
        assertThat(modeloInversor1).isNotEqualTo(modeloInversor2);
    }

    @Test
    void marcaTest() {
        ModeloInversor modeloInversor = getModeloInversorRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        modeloInversor.setMarca(marcaBack);
        assertThat(modeloInversor.getMarca()).isEqualTo(marcaBack);

        modeloInversor.marca(null);
        assertThat(modeloInversor.getMarca()).isNull();
    }
}

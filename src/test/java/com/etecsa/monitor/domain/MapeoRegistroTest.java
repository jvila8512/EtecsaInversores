package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.MapeoRegistroTestSamples.*;
import static com.etecsa.monitor.domain.MarcaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MapeoRegistroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MapeoRegistro.class);
        MapeoRegistro mapeoRegistro1 = getMapeoRegistroSample1();
        MapeoRegistro mapeoRegistro2 = new MapeoRegistro();
        assertThat(mapeoRegistro1).isNotEqualTo(mapeoRegistro2);

        mapeoRegistro2.setId(mapeoRegistro1.getId());
        assertThat(mapeoRegistro1).isEqualTo(mapeoRegistro2);

        mapeoRegistro2 = getMapeoRegistroSample2();
        assertThat(mapeoRegistro1).isNotEqualTo(mapeoRegistro2);
    }

    @Test
    void marcaTest() {
        MapeoRegistro mapeoRegistro = getMapeoRegistroRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        mapeoRegistro.setMarca(marcaBack);
        assertThat(mapeoRegistro.getMarca()).isEqualTo(marcaBack);

        mapeoRegistro.marca(null);
        assertThat(mapeoRegistro.getMarca()).isNull();
    }
}

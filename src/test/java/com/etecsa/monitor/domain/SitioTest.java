package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.SitioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SitioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sitio.class);
        Sitio sitio1 = getSitioSample1();
        Sitio sitio2 = new Sitio();
        assertThat(sitio1).isNotEqualTo(sitio2);

        sitio2.setId(sitio1.getId());
        assertThat(sitio1).isEqualTo(sitio2);

        sitio2 = getSitioSample2();
        assertThat(sitio1).isNotEqualTo(sitio2);
    }
}

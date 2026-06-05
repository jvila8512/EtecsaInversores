package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.AlertaConfigTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlertaConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertaConfig.class);
        AlertaConfig alertaConfig1 = getAlertaConfigSample1();
        AlertaConfig alertaConfig2 = new AlertaConfig();
        assertThat(alertaConfig1).isNotEqualTo(alertaConfig2);

        alertaConfig2.setId(alertaConfig1.getId());
        assertThat(alertaConfig1).isEqualTo(alertaConfig2);

        alertaConfig2 = getAlertaConfigSample2();
        assertThat(alertaConfig1).isNotEqualTo(alertaConfig2);
    }

    @Test
    void inversorTest() {
        AlertaConfig alertaConfig = getAlertaConfigRandomSampleGenerator();
        Inversor inversorBack = getInversorRandomSampleGenerator();

        alertaConfig.setInversor(inversorBack);
        assertThat(alertaConfig.getInversor()).isEqualTo(inversorBack);

        alertaConfig.inversor(null);
        assertThat(alertaConfig.getInversor()).isNull();
    }
}

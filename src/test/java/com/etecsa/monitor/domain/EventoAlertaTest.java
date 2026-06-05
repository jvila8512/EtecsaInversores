package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.AlertaConfigTestSamples.*;
import static com.etecsa.monitor.domain.EventoAlertaTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventoAlertaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventoAlerta.class);
        EventoAlerta eventoAlerta1 = getEventoAlertaSample1();
        EventoAlerta eventoAlerta2 = new EventoAlerta();
        assertThat(eventoAlerta1).isNotEqualTo(eventoAlerta2);

        eventoAlerta2.setId(eventoAlerta1.getId());
        assertThat(eventoAlerta1).isEqualTo(eventoAlerta2);

        eventoAlerta2 = getEventoAlertaSample2();
        assertThat(eventoAlerta1).isNotEqualTo(eventoAlerta2);
    }

    @Test
    void inversorTest() {
        EventoAlerta eventoAlerta = getEventoAlertaRandomSampleGenerator();
        Inversor inversorBack = getInversorRandomSampleGenerator();

        eventoAlerta.setInversor(inversorBack);
        assertThat(eventoAlerta.getInversor()).isEqualTo(inversorBack);

        eventoAlerta.inversor(null);
        assertThat(eventoAlerta.getInversor()).isNull();
    }

    @Test
    void alertaConfigTest() {
        EventoAlerta eventoAlerta = getEventoAlertaRandomSampleGenerator();
        AlertaConfig alertaConfigBack = getAlertaConfigRandomSampleGenerator();

        eventoAlerta.setAlertaConfig(alertaConfigBack);
        assertThat(eventoAlerta.getAlertaConfig()).isEqualTo(alertaConfigBack);

        eventoAlerta.alertaConfig(null);
        assertThat(eventoAlerta.getAlertaConfig()).isNull();
    }
}

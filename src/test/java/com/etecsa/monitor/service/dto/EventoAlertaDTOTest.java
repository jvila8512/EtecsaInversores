package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventoAlertaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventoAlertaDTO.class);
        EventoAlertaDTO eventoAlertaDTO1 = new EventoAlertaDTO();
        eventoAlertaDTO1.setId(1L);
        EventoAlertaDTO eventoAlertaDTO2 = new EventoAlertaDTO();
        assertThat(eventoAlertaDTO1).isNotEqualTo(eventoAlertaDTO2);
        eventoAlertaDTO2.setId(eventoAlertaDTO1.getId());
        assertThat(eventoAlertaDTO1).isEqualTo(eventoAlertaDTO2);
        eventoAlertaDTO2.setId(2L);
        assertThat(eventoAlertaDTO1).isNotEqualTo(eventoAlertaDTO2);
        eventoAlertaDTO1.setId(null);
        assertThat(eventoAlertaDTO1).isNotEqualTo(eventoAlertaDTO2);
    }
}

package com.etecsa.monitor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MapeoRegistroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MapeoRegistroDTO.class);
        MapeoRegistroDTO mapeoRegistroDTO1 = new MapeoRegistroDTO();
        mapeoRegistroDTO1.setId(1L);
        MapeoRegistroDTO mapeoRegistroDTO2 = new MapeoRegistroDTO();
        assertThat(mapeoRegistroDTO1).isNotEqualTo(mapeoRegistroDTO2);
        mapeoRegistroDTO2.setId(mapeoRegistroDTO1.getId());
        assertThat(mapeoRegistroDTO1).isEqualTo(mapeoRegistroDTO2);
        mapeoRegistroDTO2.setId(2L);
        assertThat(mapeoRegistroDTO1).isNotEqualTo(mapeoRegistroDTO2);
        mapeoRegistroDTO1.setId(null);
        assertThat(mapeoRegistroDTO1).isNotEqualTo(mapeoRegistroDTO2);
    }
}

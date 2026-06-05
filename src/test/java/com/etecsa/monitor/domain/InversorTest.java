package com.etecsa.monitor.domain;

import static com.etecsa.monitor.domain.GatewayTcpTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static com.etecsa.monitor.domain.InversorTestSamples.*;
import static com.etecsa.monitor.domain.MarcaTestSamples.*;
import static com.etecsa.monitor.domain.ModeloInversorTestSamples.*;
import static com.etecsa.monitor.domain.SitioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.etecsa.monitor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InversorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inversor.class);
        Inversor inversor1 = getInversorSample1();
        Inversor inversor2 = new Inversor();
        assertThat(inversor1).isNotEqualTo(inversor2);

        inversor2.setId(inversor1.getId());
        assertThat(inversor1).isEqualTo(inversor2);

        inversor2 = getInversorSample2();
        assertThat(inversor1).isNotEqualTo(inversor2);
    }

    @Test
    void sitioTest() {
        Inversor inversor = getInversorRandomSampleGenerator();
        Sitio sitioBack = getSitioRandomSampleGenerator();

        inversor.setSitio(sitioBack);
        assertThat(inversor.getSitio()).isEqualTo(sitioBack);

        inversor.sitio(null);
        assertThat(inversor.getSitio()).isNull();
    }

    @Test
    void marcaTest() {
        Inversor inversor = getInversorRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        inversor.setMarca(marcaBack);
        assertThat(inversor.getMarca()).isEqualTo(marcaBack);

        inversor.marca(null);
        assertThat(inversor.getMarca()).isNull();
    }

    @Test
    void modeloTest() {
        Inversor inversor = getInversorRandomSampleGenerator();
        ModeloInversor modeloInversorBack = getModeloInversorRandomSampleGenerator();

        inversor.setModelo(modeloInversorBack);
        assertThat(inversor.getModelo()).isEqualTo(modeloInversorBack);

        inversor.modelo(null);
        assertThat(inversor.getModelo()).isNull();
    }

    @Test
    void gatewayTest() {
        Inversor inversor = getInversorRandomSampleGenerator();
        GatewayTcp gatewayTcpBack = getGatewayTcpRandomSampleGenerator();

        inversor.setGateway(gatewayTcpBack);
        assertThat(inversor.getGateway()).isEqualTo(gatewayTcpBack);

        inversor.gateway(null);
        assertThat(inversor.getGateway()).isNull();
    }

    @Test
    void masterReferenciaTest() {
        Inversor inversor = getInversorRandomSampleGenerator();
        Inversor inversorBack = getInversorRandomSampleGenerator();

        inversor.setMasterReferencia(inversorBack);
        assertThat(inversor.getMasterReferencia()).isEqualTo(inversorBack);

        inversor.masterReferencia(null);
        assertThat(inversor.getMasterReferencia()).isNull();
    }
}

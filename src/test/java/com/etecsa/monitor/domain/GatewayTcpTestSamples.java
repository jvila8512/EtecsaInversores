package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GatewayTcpTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static GatewayTcp getGatewayTcpSample1() {
        return new GatewayTcp()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .ubicacion("ubicacion1")
            .direccionIp("direccionIp1")
            .puerto(1)
            .ultimoError("ultimoError1")
            .tiempoEsperaMs(1)
            .reintentos(1);
    }

    public static GatewayTcp getGatewayTcpSample2() {
        return new GatewayTcp()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .ubicacion("ubicacion2")
            .direccionIp("direccionIp2")
            .puerto(2)
            .ultimoError("ultimoError2")
            .tiempoEsperaMs(2)
            .reintentos(2);
    }

    public static GatewayTcp getGatewayTcpRandomSampleGenerator() {
        return new GatewayTcp()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .ubicacion(UUID.randomUUID().toString())
            .direccionIp(UUID.randomUUID().toString())
            .puerto(intCount.incrementAndGet())
            .ultimoError(UUID.randomUUID().toString())
            .tiempoEsperaMs(intCount.incrementAndGet())
            .reintentos(intCount.incrementAndGet());
    }
}

package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlertaConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlertaConfig getAlertaConfigSample1() {
        return new AlertaConfig()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .campoMonitorizado("campoMonitorizado1")
            .operador("operador1")
            .nivelSeveridad("nivelSeveridad1")
            .canalNotificacion("canalNotificacion1")
            .cooldownMinutos(1);
    }

    public static AlertaConfig getAlertaConfigSample2() {
        return new AlertaConfig()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .campoMonitorizado("campoMonitorizado2")
            .operador("operador2")
            .nivelSeveridad("nivelSeveridad2")
            .canalNotificacion("canalNotificacion2")
            .cooldownMinutos(2);
    }

    public static AlertaConfig getAlertaConfigRandomSampleGenerator() {
        return new AlertaConfig()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .campoMonitorizado(UUID.randomUUID().toString())
            .operador(UUID.randomUUID().toString())
            .nivelSeveridad(UUID.randomUUID().toString())
            .canalNotificacion(UUID.randomUUID().toString())
            .cooldownMinutos(intCount.incrementAndGet());
    }
}

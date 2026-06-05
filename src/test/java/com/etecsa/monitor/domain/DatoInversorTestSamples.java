package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DatoInversorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DatoInversor getDatoInversorSample1() {
        return new DatoInversor().id(1L).cargaPorcentaje(1).modoOperacion("modoOperacion1").codigoFallo(1).latenciaMs(1);
    }

    public static DatoInversor getDatoInversorSample2() {
        return new DatoInversor().id(2L).cargaPorcentaje(2).modoOperacion("modoOperacion2").codigoFallo(2).latenciaMs(2);
    }

    public static DatoInversor getDatoInversorRandomSampleGenerator() {
        return new DatoInversor()
            .id(longCount.incrementAndGet())
            .cargaPorcentaje(intCount.incrementAndGet())
            .modoOperacion(UUID.randomUUID().toString())
            .codigoFallo(intCount.incrementAndGet())
            .latenciaMs(intCount.incrementAndGet());
    }
}

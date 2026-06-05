package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ModeloInversorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ModeloInversor getModeloInversorSample1() {
        return new ModeloInversor().id(1L).nombre("nombre1").capacidadVa(1).voltajeSistemaNom(1).descripcion("descripcion1");
    }

    public static ModeloInversor getModeloInversorSample2() {
        return new ModeloInversor().id(2L).nombre("nombre2").capacidadVa(2).voltajeSistemaNom(2).descripcion("descripcion2");
    }

    public static ModeloInversor getModeloInversorRandomSampleGenerator() {
        return new ModeloInversor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .capacidadVa(intCount.incrementAndGet())
            .voltajeSistemaNom(intCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}

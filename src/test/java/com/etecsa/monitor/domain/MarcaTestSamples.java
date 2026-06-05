package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MarcaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Marca getMarcaSample1() {
        return new Marca()
            .id(1L)
            .nombre("nombre1")
            .fabricante("fabricante1")
            .registroInicio(1)
            .registroFin(1)
            .registroInfoInicio(1)
            .registroInfoFin(1)
            .baudRateDefault(1)
            .descripcion("descripcion1");
    }

    public static Marca getMarcaSample2() {
        return new Marca()
            .id(2L)
            .nombre("nombre2")
            .fabricante("fabricante2")
            .registroInicio(2)
            .registroFin(2)
            .registroInfoInicio(2)
            .registroInfoFin(2)
            .baudRateDefault(2)
            .descripcion("descripcion2");
    }

    public static Marca getMarcaRandomSampleGenerator() {
        return new Marca()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .fabricante(UUID.randomUUID().toString())
            .registroInicio(intCount.incrementAndGet())
            .registroFin(intCount.incrementAndGet())
            .registroInfoInicio(intCount.incrementAndGet())
            .registroInfoFin(intCount.incrementAndGet())
            .baudRateDefault(intCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}

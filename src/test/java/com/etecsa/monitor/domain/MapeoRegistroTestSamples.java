package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MapeoRegistroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MapeoRegistro getMapeoRegistroSample1() {
        return new MapeoRegistro()
            .id(1L)
            .direccionRegistro(1)
            .nombreRegistro("nombreRegistro1")
            .campoSql("campoSql1")
            .unidad("unidad1")
            .descripcion("descripcion1");
    }

    public static MapeoRegistro getMapeoRegistroSample2() {
        return new MapeoRegistro()
            .id(2L)
            .direccionRegistro(2)
            .nombreRegistro("nombreRegistro2")
            .campoSql("campoSql2")
            .unidad("unidad2")
            .descripcion("descripcion2");
    }

    public static MapeoRegistro getMapeoRegistroRandomSampleGenerator() {
        return new MapeoRegistro()
            .id(longCount.incrementAndGet())
            .direccionRegistro(intCount.incrementAndGet())
            .nombreRegistro(UUID.randomUUID().toString())
            .campoSql(UUID.randomUUID().toString())
            .unidad(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}

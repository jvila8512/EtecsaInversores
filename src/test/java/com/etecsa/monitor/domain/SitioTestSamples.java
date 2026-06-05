package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SitioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sitio getSitioSample1() {
        return new Sitio()
            .id(1L)
            .nombre("nombre1")
            .descripcion("descripcion1")
            .ubicacion("ubicacion1")
            .contacto("contacto1")
            .telefono("telefono1");
    }

    public static Sitio getSitioSample2() {
        return new Sitio()
            .id(2L)
            .nombre("nombre2")
            .descripcion("descripcion2")
            .ubicacion("ubicacion2")
            .contacto("contacto2")
            .telefono("telefono2");
    }

    public static Sitio getSitioRandomSampleGenerator() {
        return new Sitio()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .ubicacion(UUID.randomUUID().toString())
            .contacto(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}

package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InversorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Inversor getInversorSample1() {
        return new Inversor()
            .id(1L)
            .nombre("nombre1")
            .numeroSerie("numeroSerie1")
            .puertoSerial("puertoSerial1")
            .slaveId(1)
            .ultimoError("ultimoError1")
            .periodoMuestreoSeg(1);
    }

    public static Inversor getInversorSample2() {
        return new Inversor()
            .id(2L)
            .nombre("nombre2")
            .numeroSerie("numeroSerie2")
            .puertoSerial("puertoSerial2")
            .slaveId(2)
            .ultimoError("ultimoError2")
            .periodoMuestreoSeg(2);
    }

    public static Inversor getInversorRandomSampleGenerator() {
        return new Inversor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .numeroSerie(UUID.randomUUID().toString())
            .puertoSerial(UUID.randomUUID().toString())
            .slaveId(intCount.incrementAndGet())
            .ultimoError(UUID.randomUUID().toString())
            .periodoMuestreoSeg(intCount.incrementAndGet());
    }
}

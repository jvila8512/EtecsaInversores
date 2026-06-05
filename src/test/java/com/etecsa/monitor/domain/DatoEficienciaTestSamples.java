package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DatoEficienciaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DatoEficiencia getDatoEficienciaSample1() {
        return new DatoEficiencia().id(1L).totalLecturas(1);
    }

    public static DatoEficiencia getDatoEficienciaSample2() {
        return new DatoEficiencia().id(2L).totalLecturas(2);
    }

    public static DatoEficiencia getDatoEficienciaRandomSampleGenerator() {
        return new DatoEficiencia().id(longCount.incrementAndGet()).totalLecturas(intCount.incrementAndGet());
    }
}

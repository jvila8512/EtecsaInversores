package com.etecsa.monitor.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventoAlertaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventoAlerta getEventoAlertaSample1() {
        return new EventoAlerta()
            .id(1L)
            .nombreAlerta("nombreAlerta1")
            .mensaje("mensaje1")
            .campoMonitorizado("campoMonitorizado1")
            .operador("operador1")
            .nivelSeveridad("nivelSeveridad1")
            .canalUsado("canalUsado1")
            .notas("notas1");
    }

    public static EventoAlerta getEventoAlertaSample2() {
        return new EventoAlerta()
            .id(2L)
            .nombreAlerta("nombreAlerta2")
            .mensaje("mensaje2")
            .campoMonitorizado("campoMonitorizado2")
            .operador("operador2")
            .nivelSeveridad("nivelSeveridad2")
            .canalUsado("canalUsado2")
            .notas("notas2");
    }

    public static EventoAlerta getEventoAlertaRandomSampleGenerator() {
        return new EventoAlerta()
            .id(longCount.incrementAndGet())
            .nombreAlerta(UUID.randomUUID().toString())
            .mensaje(UUID.randomUUID().toString())
            .campoMonitorizado(UUID.randomUUID().toString())
            .operador(UUID.randomUUID().toString())
            .nivelSeveridad(UUID.randomUUID().toString())
            .canalUsado(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}

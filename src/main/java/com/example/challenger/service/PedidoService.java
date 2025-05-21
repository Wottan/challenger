package com.example.challenger.service;

import com.example.challenger.model.PedidoRequest;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PedidoService {

    private final Map<String, PedidoRequest> processedOrders = new ConcurrentHashMap<>();
    private final Timer processingTimer;

    public PedidoService(MeterRegistry meterRegistry) {
        this.processingTimer = meterRegistry.timer("order.processing.timer");
    }

    public Mono<String> procesarPedido(PedidoRequest pedidoRequest) {
        long start = System.currentTimeMillis();

        return Mono.delay(Duration.ofMillis(ThreadLocalRandom.current().nextInt(100, 500)))
                .doOnNext(i -> {

                    processedOrders.put(pedidoRequest.orderId(), pedidoRequest);

                    long end = System.currentTimeMillis();

                    processingTimer.record(end - start, java.util.concurrent.TimeUnit.MILLISECONDS);
                    System.out.printf("Pedido %s procesado en %d ms%n", pedidoRequest.orderId(), (end - start));
                })
                .thenReturn("Pedido procesado: " + pedidoRequest.orderId());
    }
}

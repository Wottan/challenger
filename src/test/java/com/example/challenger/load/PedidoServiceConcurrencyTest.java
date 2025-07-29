package com.example.challenger.load;

import com.example.challenger.dto.PedidoRequest;
import com.example.challenger.model.OrderItem;
import com.example.challenger.model.PedidoProcesado;
import com.example.challenger.service.PedidoService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PedidoServiceConcurrencyTest {

    private PedidoService pedidoService;
    private Counter pedidoCounter;

    @BeforeEach
    void setup() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        pedidoService = new PedidoService(registry);
        pedidoCounter = registry.counter("pedidos_procesados_total");
    }

    @Test
    void testConcurrentPedidos() {
        int totalPedidos = 1000;
        int concurrencia = 100;

        long startTime = System.currentTimeMillis();

        Flux<PedidoProcesado> pedidos = Flux.range(1, totalPedidos)
                .flatMap(i -> pedidoService.procesarPedido(
                        new PedidoRequest(
                                "order-" + i,
                                100.0,
                                "customer-" + i,
                                List.of(new OrderItem("item-" + i, 10.0))
                        )
                ), concurrencia)
                .timeout(Duration.ofSeconds(30));

        StepVerifier.create(pedidos)
                .expectNextCount(totalPedidos)
                .verifyComplete();

        long totalTime = System.currentTimeMillis() - startTime;
        double throughput = totalPedidos / (totalTime / 1000.0);

        System.out.printf("Tiempo total de procesamiento: %d ms%n", totalTime);
        System.out.printf("Throughput aproximado: %.2f req/s%n", throughput);

        assertEquals(totalPedidos, (int) pedidoCounter.count());
    }
}

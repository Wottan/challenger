package com.example.challenger.service;

import com.example.challenger.model.PedidoRequest;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.List;

class PedidoServiceTest {

    private final PedidoService pedidoService = new PedidoService(new SimpleMeterRegistry());

    @Test
    void testValidOrderProcessing() {
        PedidoRequest request = new PedidoRequest("1",
                100.0,
                "cust",
                List.of("item1", "item2"));

        StepVerifier.create(pedidoService.procesarPedido(request))
                .expectNext("Pedido procesado: 1")
                .verifyComplete();
    }

}
package com.example.challenger.service;

import com.example.challenger.dto.PedidoRequest;
import com.example.challenger.model.OrderItem;
import com.example.challenger.model.PedidoProcesado;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.util.List;

class PedidoServiceTest {

    private final PedidoService pedidoService = new PedidoService(new SimpleMeterRegistry());

    @Test
    void testValidOrderProcessing() {
        PedidoProcesado expected = new PedidoProcesado("1",
                "cust",
                130.0);
        PedidoRequest request = new PedidoRequest("1",
                100.0,
                "cust",
                List.of(new OrderItem("uno", 10.0),
                        new OrderItem("dos", 20.0)));

        StepVerifier.create(pedidoService.procesarPedido(request))
                .expectNext(expected)
                .verifyComplete();
    }

}
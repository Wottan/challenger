package com.example.challenger.controller;

import com.example.challenger.dto.PedidoRequest;
import com.example.challenger.model.OrderItem;
import com.example.challenger.model.PedidoProcesado;
import com.example.challenger.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;

class PedidoControllerTest {
    private WebTestClient webTestClient;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = Mockito.mock(PedidoService.class);
        PedidoController pedidoController = new PedidoController(pedidoService);

        webTestClient = WebTestClient.bindToController(pedidoController)
                .configureClient()
                .baseUrl("/")
                .build();
    }

    @Test
    void procesarPedido_debeRetornarPedidoProcesado() {

        PedidoRequest request = new PedidoRequest("ord1",
                100.1,
                "id",
                List.of(new OrderItem("uno", 10.0),
                        new OrderItem("dos", 20.0))
        );

        PedidoProcesado procesado = new PedidoProcesado("ord1",
                "id",
                130.1);

        Mockito.when(pedidoService.procesarPedido(Mockito.any()))
                .thenReturn(Mono.just(procesado));

        webTestClient.post()
                .uri("/processOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo("ord1")
                .jsonPath("$.totalPrice").isEqualTo(130.1);
    }
}
package com.example.challenger.exception;

import com.example.challenger.controller.PedidoController;
import com.example.challenger.model.PedidoRequest;
import com.example.challenger.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = PedidoController.class)
class PedidoValidationTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    public void testValidationErrorMessages() {
        // Datos inválidos
        PedidoRequest invalidRequest = new PedidoRequest(null, 0.0, "", List.of());
        webTestClient.post()
                .uri("/processOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Map.class)
                .value(errors -> {
                    System.out.println(errors);
                    assertThat(errors)
                            .containsEntry("orderId", "no debe ser nulo")
                            .containsEntry("orderItems","no debe estar vacío");
                });
    }

}
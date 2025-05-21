package com.example.challenger.controller;

import com.example.challenger.model.PedidoRequest;
import com.example.challenger.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/processOrder")
    public Mono<ResponseEntity<String>> procesarPedido(@Valid @RequestBody Mono<PedidoRequest> pedidoRequest) {
        return pedidoRequest
                .flatMap(pedidoService::procesarPedido)
                .map(ResponseEntity::ok);
    }
}

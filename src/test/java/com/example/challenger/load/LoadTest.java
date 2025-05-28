package com.example.challenger.load;

import com.example.challenger.dto.PedidoRequest;
import com.example.challenger.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

class LoadTest {
    private final WebClient client = WebClient.create("http://localhost:8080");

    @Test
    void testConcurrentOrders() {
        Flux.range(1, 1000)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(i -> {
                    PedidoRequest order = new PedidoRequest(UUID.randomUUID().toString(),
                            100.0,
                            "cust" + i,
                            List.of(new OrderItem("uno", 10.0),
                                    new OrderItem("dos", 20.0)));

                    return client.post()
                            .uri("/processOrder")
                            .body(BodyInserters.fromValue(order))
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnNext(System.out::println);
                })
                .sequential()
                .blockLast();
    }

//    @Test
//    void testConcurrentOrders() {
//        // Mock WebClient and its fluent API
//        WebClient client = mock(WebClient.class);
//        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
//        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
//        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
//
//        when(client.post()).thenReturn(uriSpec);
//        when(uriSpec.uri(eq("/processOrder"))).thenReturn(bodySpec);
//        when(bodySpec.body(any(BodyInserter.class))).thenReturn(bodySpec);
//        when(bodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Pedido procesado"));
//
//        // Flux simulando las 1000 solicitudes concurrentes
//        Flux<String> flux = Flux.range(1, 1000)
//                .parallel()
//                .runOn(Schedulers.parallel())
//                //.subscribeOn(Schedulers.boundedElastic()) // con esta linea lo realizo de manera bloqueante
//                .flatMap(i -> {
//                    PedidoRequest order = new PedidoRequest(UUID.randomUUID().toString(),
//                            100.0,
//                            "cust" + i,
//                            List.of(new OrderItem("uno",10.0),
//                                    new OrderItem("dos",20.0)));
//
//                    return client.post()
//                            .uri("/processOrder")
//                            .body(BodyInserters.fromValue(order))
//                            .retrieve()
//                            .bodyToMono(String.class);
//                })
//                .sequential();
//
//        // Verificar que el flujo produce el resultado esperado
//        StepVerifier.create(flux)
//                .expectNextCount(1000)
//                .verifyComplete();
//
//        // Verificar que el método post fue llamado (al menos una vez)
//        verify(client, atLeastOnce()).post();
//    }
}

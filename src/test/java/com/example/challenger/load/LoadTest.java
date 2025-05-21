package com.example.challenger.load;

import com.example.challenger.model.PedidoRequest;
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
                            List.of("item1", "item2"));

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
}

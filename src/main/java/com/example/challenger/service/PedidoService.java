package com.example.challenger.service;

import com.example.challenger.dto.PedidoRequest;
import com.example.challenger.model.OrderItem;
import com.example.challenger.model.PedidoProcesado;
import com.example.challenger.storage.PedidoStorage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private final Timer pedidoTiempo;
    private final Counter pedidoContador;

    public PedidoService(MeterRegistry meterRegistry) {
        this.pedidoTiempo = meterRegistry.timer("pedido_procesamiento_tiempo");
        this.pedidoContador = meterRegistry.counter("pedidos_procesados_total");
    }

    public Mono<PedidoProcesado> procesarPedido(PedidoRequest pedidoRequest) {

        long start = System.currentTimeMillis();

        return validateOrder(pedidoRequest)
                .flatMap(this::simulateBusinessLogic)
                .doOnNext(pedidoProcesado -> {
                    long end = System.currentTimeMillis();
                    pedidoTiempo.record(end - start, java.util.concurrent.TimeUnit.MILLISECONDS);
                    pedidoContador.increment();
                    logger.info("Pedido [{}] procesado en {} ms", pedidoRequest.orderId(), (end - start));
                })
                .onErrorResume(ex -> {
                    logger.error("Error procesando pedido [{}]: {}", pedidoRequest.orderId(), ex.getMessage(), ex);
                    return Mono.error(ex);
                });
    }

    private Double calcularPrecio(PedidoRequest pedidoRequest) {
        return pedidoRequest.orderAmount() + pedidoRequest.orderItems().stream().mapToDouble(OrderItem::price).sum();
    }

    private Mono<PedidoProcesado> simulateBusinessLogic(PedidoRequest pedidoRequest) {
        int delay = ThreadLocalRandom.current().nextInt(100, 500);
        double total = calcularPrecio(pedidoRequest);

        PedidoProcesado pedidoProcesado = new PedidoProcesado(
                pedidoRequest.orderId(),
                pedidoRequest.customerId(),
                total);

        PedidoStorage.guardar(pedidoProcesado);
        return Mono.delay(Duration.ofMillis(delay)).thenReturn(pedidoProcesado);
    }


    private Mono<PedidoRequest> validateOrder(PedidoRequest order) {
        if (order.orderAmount() == null || order.orderAmount() <= 0
                || order.orderItems() == null || order.orderItems().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Invalid order: " + order.orderId()));
        }
        return Mono.just(order);
    }

}

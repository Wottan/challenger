package com.example.challenger.storage;

import com.example.challenger.model.PedidoProcesado;

import java.util.concurrent.ConcurrentHashMap;

public class PedidoStorage {
    private static final ConcurrentHashMap<String, PedidoProcesado> pedidosProcesados = new ConcurrentHashMap<>();

    public static void guardar(PedidoProcesado processOrder) {
        pedidosProcesados.put(processOrder.orderId(), processOrder);
    }

    public static ConcurrentHashMap<String, PedidoProcesado> getAllOrders() {
        return pedidosProcesados;
    }
}

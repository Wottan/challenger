package com.example.challenger.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(@NotNull String orderId,
                            Double customerAmount,
                            String customerId,
                            @NotEmpty List<String> orderItems) {
}

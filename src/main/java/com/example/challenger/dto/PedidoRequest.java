package com.example.challenger.dto;

import com.example.challenger.model.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(@NotNull @NotBlank String orderId,
                            Double orderAmount,
                            String customerId,
                            @NotEmpty List<OrderItem> orderItems) {
}

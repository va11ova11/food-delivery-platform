package com.mycompany.fooddeliveryplatform.dto.order;

import com.mycompany.fooddeliveryplatform.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long restaurantId,
        Long customerId,
        String deliveryAddress,
        BigDecimal totalPrice,
        OrderStatus status,
        Instant createdAt,
        List<OrderItemShortDto> items
) {}
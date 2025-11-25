package com.mycompany.fooddeliveryplatform.dto.order;

import java.math.BigDecimal;

public record OrderItemShortDto(
        Long menuItemId,
        String name,
        int quantity,
        BigDecimal price
) {}
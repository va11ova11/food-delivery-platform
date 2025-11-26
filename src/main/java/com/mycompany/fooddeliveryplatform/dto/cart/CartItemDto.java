package com.mycompany.fooddeliveryplatform.dto.cart;


import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        Long menuItemId,
        String name,
        int quantity,
        BigDecimal price
) {}
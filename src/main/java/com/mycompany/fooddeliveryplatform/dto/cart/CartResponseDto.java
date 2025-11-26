package com.mycompany.fooddeliveryplatform.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        Long id,
        Long restaurantId,
        String restaurantName,
        BigDecimal totalPrice,
        List<CartItemDto> items
) {}
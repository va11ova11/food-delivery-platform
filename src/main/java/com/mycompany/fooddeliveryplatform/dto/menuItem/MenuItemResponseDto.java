package com.mycompany.fooddeliveryplatform.dto.menuItem;

import java.math.BigDecimal;

public record MenuItemResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available
) {
}

package com.mycompany.fooddeliveryplatform.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull Long menuItemId,
        @Min(1) int quantity
) {}
package com.mycompany.fooddeliveryplatform.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(
        @NotNull
        Long menuItemId,

        @Min(1)
        int quantity
) {}
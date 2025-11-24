package com.mycompany.fooddeliveryplatform.dto.menuItem;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequestDto(
        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 1000)
        String description,

        @DecimalMin("0.0")
        BigDecimal price,

        boolean available
) {}
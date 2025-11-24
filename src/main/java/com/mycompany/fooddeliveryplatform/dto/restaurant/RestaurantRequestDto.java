package com.mycompany.fooddeliveryplatform.dto.restaurant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RestaurantRequestDto(
        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 1000)
        String description,

        @Size(max = 255)
        String address,

        @Size(max = 100)
        String workingHours,

        @DecimalMin(value = "0.0")
        BigDecimal minOrderPrice,

        boolean active
) {}
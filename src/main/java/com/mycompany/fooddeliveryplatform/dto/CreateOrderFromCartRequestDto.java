package com.mycompany.fooddeliveryplatform.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderFromCartRequestDto(
        @NotBlank String deliveryAddress
) {}

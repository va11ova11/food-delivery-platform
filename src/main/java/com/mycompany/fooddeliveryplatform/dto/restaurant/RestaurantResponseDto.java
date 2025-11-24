package com.mycompany.fooddeliveryplatform.dto.restaurant;

import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record RestaurantResponseDto(
        Long id,
        String name,
        String description,
        String address,
        String workingHours,
        BigDecimal minOrderPrice,
        boolean active,
        List<MenuItemResponseDto> menuItems
) {}

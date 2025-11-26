package com.mycompany.fooddeliveryplatform.mapper;

import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemResponseDto;
import com.mycompany.fooddeliveryplatform.model.restaurant.MenuItem;
import com.mycompany.fooddeliveryplatform.model.restaurant.Restaurant;

public class MenuItemMapper {

    public static MenuItem toEntity(MenuItemRequestDto dto, Restaurant restaurant) {
        if (dto == null) {
            return null;
        }
        MenuItem item = new MenuItem();
        item.setRestaurant(restaurant);
        item.setName(dto.name());
        item.setDescription(dto.description());
        item.setPrice(dto.price());
        item.setAvailable(dto.available());
        return item;
    }

    public static MenuItemResponseDto toDto(MenuItem item) {
        if (item == null) {
            return null;
        }
        return new MenuItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.isAvailable()
        );
    }
}
package com.mycompany.fooddeliveryplatform.mapper;

import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemResponseDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantRequestDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantResponseDto;
import com.mycompany.fooddeliveryplatform.model.Restaurant;

import java.util.List;

public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.name());
        restaurant.setDescription(dto.description());
        restaurant.setAddress(dto.address());
        restaurant.setWorkingHours(dto.workingHours());
        restaurant.setMinOrderPrice(dto.minOrderPrice());
        restaurant.setActive(dto.active());
        return restaurant;
    }

    public static void updateEntity(Restaurant restaurant, RestaurantRequestDto dto) {
        restaurant.setName(dto.name());
        restaurant.setDescription(dto.description());
        restaurant.setAddress(dto.address());
        restaurant.setWorkingHours(dto.workingHours());
        restaurant.setMinOrderPrice(dto.minOrderPrice());
        restaurant.setActive(dto.active());
    }

    public static RestaurantResponseDto toDto(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        List<MenuItemResponseDto> menuItemDtos = null;
        if (restaurant.getMenuItems() != null) {
            menuItemDtos = restaurant.getMenuItems().stream()
                    .map(MenuItemMapper::toDto)
                    .toList();
        }
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getAddress(),
                restaurant.getWorkingHours(),
                restaurant.getMinOrderPrice(),
                restaurant.isActive(),
                menuItemDtos
        );
    }
}
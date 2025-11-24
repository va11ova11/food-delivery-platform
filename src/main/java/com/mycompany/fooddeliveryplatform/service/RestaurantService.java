package com.mycompany.fooddeliveryplatform.service;

import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemResponseDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantRequestDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantResponseDto;
import com.mycompany.fooddeliveryplatform.mapper.MenuItemMapper;
import com.mycompany.fooddeliveryplatform.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import com.mycompany.fooddeliveryplatform.model.MenuItem;
import com.mycompany.fooddeliveryplatform.model.Restaurant;
import org.springframework.stereotype.Service;
import com.mycompany.fooddeliveryplatform.repository.MenuItemRepository;
import com.mycompany.fooddeliveryplatform.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;


    public List<RestaurantResponseDto> getRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::toDto)
                .toList();
    }

    public RestaurantResponseDto getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("Restaurant not found: " + restaurantId)
        );
        return RestaurantMapper.toDto(restaurant);
    }


    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = RestaurantMapper.toEntity(restaurantRequestDto);
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantMapper.toDto(saved);
    }

    public RestaurantResponseDto updateRestaurant(Long restaurantId, RestaurantRequestDto restaurantRequestDto) {
        Restaurant existing = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));
        RestaurantMapper.updateEntity(existing, restaurantRequestDto);
        Restaurant saved = restaurantRepository.save(existing);
        return RestaurantMapper.toDto(saved);

    }

    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

    public List<MenuItemResponseDto> getMenu(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(MenuItemMapper::toDto)
                .toList();
    }

    public MenuItemResponseDto addMenuItem(Long restaurantId, MenuItemRequestDto menuItemRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));
        MenuItem menuItem = MenuItemMapper.toEntity(menuItemRequestDto, restaurant);
        MenuItem saved = menuItemRepository.save(menuItem);
        return MenuItemMapper.toDto(saved);
    }
}

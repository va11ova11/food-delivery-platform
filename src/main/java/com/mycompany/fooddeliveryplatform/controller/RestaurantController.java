package com.mycompany.fooddeliveryplatform.controller;

import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.menuItem.MenuItemResponseDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantRequestDto;
import com.mycompany.fooddeliveryplatform.dto.restaurant.RestaurantResponseDto;
import com.mycompany.fooddeliveryplatform.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantResponseDto> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    @GetMapping("{restaurantId}")
    public RestaurantResponseDto getRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @PostMapping
    public RestaurantResponseDto createRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto){
        return restaurantService.createRestaurant(restaurantRequestDto);
    }

    @PutMapping("/{restaurantId}")
    public RestaurantResponseDto updateRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody RestaurantRequestDto restaurantRequestDto){
        return restaurantService.updateRestaurant(restaurantId, restaurantRequestDto);
    }

    @GetMapping("/{restaurantId}/menu")
    public List<MenuItemResponseDto> getMenu(@PathVariable Long restaurantId) {
        return restaurantService.getMenu(restaurantId);
    }


    @PostMapping("/{restaurantId}/menu")
    public MenuItemResponseDto addMenuItem(@PathVariable Long restaurantId, @Valid @RequestBody MenuItemRequestDto menuItemRequestDto) {
        return restaurantService.addMenuItem(restaurantId, menuItemRequestDto);
    }

    @DeleteMapping("{restaurantId}")
    public void deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }

}

package com.mycompany.fooddeliveryplatform.controller;

import com.mycompany.fooddeliveryplatform.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import com.mycompany.fooddeliveryplatform.model.MenuItem;
import com.mycompany.fooddeliveryplatform.model.Restaurant;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<Restaurant> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    @GetMapping("{restaurantId}")
    public Restaurant getRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurant(restaurantId);
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant){
        return restaurantService.createRestaurant(restaurant);
    }

    @PutMapping("/{restaurantId}")
    public Restaurant updateRestaurant(@PathVariable Long restaurantId, @RequestBody Restaurant restaurant){
        return restaurantService.updateRestaurant(restaurantId, restaurant);
    }

    @GetMapping("/{restaurantId}/menu")
    public List<MenuItem> getMenu(@PathVariable Long restaurantId) {
        return restaurantService.getMenu(restaurantId);
    }


    @PostMapping("/{restaurantId}/menu")
    public MenuItem addMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItem menuItem) {
        return restaurantService.addMenuItem(restaurantId, menuItem);
    }

    @DeleteMapping("{restaurantId}")
    public void deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }

}

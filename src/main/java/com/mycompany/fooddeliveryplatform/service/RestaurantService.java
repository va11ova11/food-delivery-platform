package com.mycompany.fooddeliveryplatform.service;

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


    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalArgumentException("Restaurant not found: " + restaurantId)
        );
    }


    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long restaurantId, Restaurant updated) {
        Restaurant existing = getRestaurant(restaurantId);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setAddress(updated.getAddress());
        existing.setWorkingHours(updated.getWorkingHours());
        existing.setMinOrderPrice(updated.getMinOrderPrice());
        existing.setActive(updated.isActive());
        return restaurantRepository.save(existing);
    }

    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

    public List<MenuItem> getMenu(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = getRestaurant(restaurantId);
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem);
    }
}

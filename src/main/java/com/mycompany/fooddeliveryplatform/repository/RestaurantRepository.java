package com.mycompany.fooddeliveryplatform.repository;

import com.mycompany.fooddeliveryplatform.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
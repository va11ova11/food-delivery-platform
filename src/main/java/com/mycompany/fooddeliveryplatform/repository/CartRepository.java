package com.mycompany.fooddeliveryplatform.repository;

import com.mycompany.fooddeliveryplatform.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerId(Long customerId);
}
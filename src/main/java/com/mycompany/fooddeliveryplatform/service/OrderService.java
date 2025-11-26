package com.mycompany.fooddeliveryplatform.service;

import com.mycompany.fooddeliveryplatform.dto.CreateOrderFromCartRequestDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderItemShortDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderResponseDto;
import com.mycompany.fooddeliveryplatform.exception.OrderValidationException;
import com.mycompany.fooddeliveryplatform.exception.ResourceNotFoundException;
import com.mycompany.fooddeliveryplatform.model.*;
import com.mycompany.fooddeliveryplatform.model.cart.Cart;
import com.mycompany.fooddeliveryplatform.model.cart.CartItem;
import com.mycompany.fooddeliveryplatform.model.restaurant.MenuItem;
import com.mycompany.fooddeliveryplatform.model.restaurant.Restaurant;
import com.mycompany.fooddeliveryplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;


    @Transactional
    public OrderResponseDto createOrderFromCart() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found: " + email)
        );

        if (user.getAddress() == null || user.getAddress().isBlank()) {
            throw new OrderValidationException("User has no delivery address");
        }

        Cart cart = cartRepository.findByCustomerId(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart is empty")
        );

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new OrderValidationException("Cart is empty");
        }

        Restaurant restaurant = cart.getRestaurant();
        if (restaurant == null) {
            throw new OrderValidationException("Cart has no restaurant");
        }

        Order order = new Order();
        order.setCustomer(user);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryAddress(user.getAddress());
        order.setCreatedAt(Instant.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()) {
            MenuItem menuItem = ci.getMenuItem();

            if (menuItem == null) {
                throw new OrderValidationException("Cart item: " + ci.getId() + " has no menu item");
            }

            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new OrderValidationException("Cart contains items from different restaurants");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(menuItem.getPrice());

            orderItems.add(orderItem);

            total = total.add(
                    menuItem.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()))
            );
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);


        Order orderSaved = orderRepository.save(order);

        cartRepository.delete(cart);

        return toDto(orderSaved);
    }





    @Transactional(readOnly=true)
    public List<OrderResponseDto> getMyOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return orderRepository.findByCustomerId(customer.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public OrderResponseDto createOrderFromCart(CreateOrderFromCartRequestDto request) {
        //текущий пользователь
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        //корзина пользователя
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new OrderValidationException("Cart is empty");
        }

        Restaurant restaurant = cart.getRestaurant();
        if (restaurant == null) {
            throw new OrderValidationException("Cart has no restaurant");
        }


        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryAddress(request.deliveryAddress());
        order.setCreatedAt(Instant.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            MenuItem menuItem = cartItem.getMenuItem();

            if (menuItem == null) {
                throw new OrderValidationException("Cart item " + cartItem.getId() + " has no menu item");
            }


            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new OrderValidationException(
                        "Cart contains items from different restaurants"
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(menuItem.getPrice());

            orderItems.add(orderItem);

            total = total.add(
                    menuItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        //удаляем корзину
        cartRepository.delete(cart);

        return toDto(saved);
    }


    private OrderResponseDto toDto(Order order) {
        List<OrderItemShortDto> items = order.getItems().stream()
                .map(oi -> new OrderItemShortDto(
                        oi.getMenuItem().getId(),
                        oi.getMenuItem().getName(),
                        oi.getQuantity(),
                        oi.getPrice()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getRestaurant().getId(),
                order.getCustomer().getId(),
                order.getDeliveryAddress(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }
}
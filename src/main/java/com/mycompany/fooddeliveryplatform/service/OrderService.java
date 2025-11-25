package com.mycompany.fooddeliveryplatform.service;

import com.mycompany.fooddeliveryplatform.dto.order.CreateOrderRequestDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderItemShortDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderResponseDto;
import com.mycompany.fooddeliveryplatform.exception.OrderValidationException;
import com.mycompany.fooddeliveryplatform.exception.ResourceNotFoundException;
import com.mycompany.fooddeliveryplatform.model.*;
import com.mycompany.fooddeliveryplatform.repository.MenuItemRepository;
import com.mycompany.fooddeliveryplatform.repository.OrderRepository;
import com.mycompany.fooddeliveryplatform.repository.RestaurantRepository;
import com.mycompany.fooddeliveryplatform.repository.UserRepository;
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

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + request.restaurantId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryAddress(request.deliveryAddress());
        order.setCreatedAt(Instant.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemReq : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.menuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + itemReq.menuItemId()));

            if (!menuItem.getRestaurant().getId().equals(request.restaurantId())) {
                throw new OrderValidationException(
                        "Menu item " + itemReq.menuItemId() +
                                " does not belong to restaurant " + request.restaurantId()
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemReq.quantity());
            orderItem.setPrice(menuItem.getPrice());

            orderItems.add(orderItem);

            total = total.add(
                    menuItem.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()))
            );
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);

        return toDto(saved);
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
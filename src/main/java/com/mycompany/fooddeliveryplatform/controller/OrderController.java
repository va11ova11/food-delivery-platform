package com.mycompany.fooddeliveryplatform.controller;

import com.mycompany.fooddeliveryplatform.dto.CreateOrderFromCartRequestDto;
import com.mycompany.fooddeliveryplatform.dto.order.OrderResponseDto;
import com.mycompany.fooddeliveryplatform.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/from-cart")
    public OrderResponseDto createOrderFromCart() {
        return orderService.createOrderFromCart();
    }



    @GetMapping("/my")
    public List<OrderResponseDto> getMyOrders() {
        return orderService.getMyOrders();
    }
}
package com.mycompany.fooddeliveryplatform.controller;

import com.mycompany.fooddeliveryplatform.dto.cart.CartItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.cart.CartResponseDto;
import com.mycompany.fooddeliveryplatform.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // получить корзину текущего пользователя
    @GetMapping
    public CartResponseDto getMyCart() {
        return cartService.getMyCart();
    }

    // добавить позицию в корзину
    @PostMapping("/items")
    public CartResponseDto addItem(@Valid @RequestBody CartItemRequestDto request) {
        return cartService.addItem(request);
    }

    // изменить количество (quantity передаём как ?quantity=3)
    @PatchMapping("/items/{itemId}")
    public CartResponseDto updateItem(@PathVariable Long itemId,
                                      @RequestParam int quantity) {
        return cartService.updateItem(itemId, quantity);
    }

    // удалить позицию
    @DeleteMapping("/items/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
    }

    // очистить корзину целиком
    @DeleteMapping
    public void clearCart() {
        cartService.clearCart();
    }
}
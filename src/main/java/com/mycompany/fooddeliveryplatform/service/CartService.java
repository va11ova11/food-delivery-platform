package com.mycompany.fooddeliveryplatform.service;

import com.mycompany.fooddeliveryplatform.dto.cart.CartItemDto;
import com.mycompany.fooddeliveryplatform.dto.cart.CartItemRequestDto;
import com.mycompany.fooddeliveryplatform.dto.cart.CartResponseDto;
import com.mycompany.fooddeliveryplatform.exception.OrderValidationException;
import com.mycompany.fooddeliveryplatform.exception.ResourceNotFoundException;
import com.mycompany.fooddeliveryplatform.model.User;
import com.mycompany.fooddeliveryplatform.model.cart.Cart;
import com.mycompany.fooddeliveryplatform.model.cart.CartItem;
import com.mycompany.fooddeliveryplatform.model.restaurant.MenuItem;
import com.mycompany.fooddeliveryplatform.repository.CartRepository;
import com.mycompany.fooddeliveryplatform.repository.MenuItemRepository;
import com.mycompany.fooddeliveryplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public CartResponseDto getMyCart() {
        User customer = getCurrentUser();
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        return toDto(cart);
    }

    @Transactional
    public CartResponseDto addItem(CartItemRequestDto request) {
        User customer = getCurrentUser();

        MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + request.menuItemId()));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElse(null);

        // если корзины нет — создаём
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cart.setRestaurant(menuItem.getRestaurant());
            cart.setItems(new ArrayList<>());
        } else if (!cart.getRestaurant().getId().equals(menuItem.getRestaurant().getId())) {
            // уже есть корзина с другим рестораном
            throw new OrderValidationException("Cart already contains items from another restaurant");
        }

        if (request.quantity() <= 0) {
            throw new OrderValidationException("Quantity must be positive");
        }

        // ищем, есть ли уже такой товар в корзине
        CartItem existing = cart.getItems().stream()
                .filter(ci -> ci.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.quantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setMenuItem(menuItem);
            newItem.setQuantity(request.quantity());
            cart.getItems().add(newItem);
        }

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    @Transactional
    public CartResponseDto updateItem(Long itemId, int quantity) {
        if (quantity < 0) {
            throw new OrderValidationException("Quantity cannot be negative");
        }

        User customer = getCurrentUser();
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));

        if (quantity == 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart);
            // корзина опустела — в следующий раз вернётся 404 "Cart is empty"
            throw new ResourceNotFoundException("Cart is now empty");
        }

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    @Transactional
    public void removeItem(Long itemId) {
        User customer = getCurrentUser();
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

        boolean removed = cart.getItems().removeIf(ci -> ci.getId().equals(itemId));

        if (!removed) {
            throw new ResourceNotFoundException("Cart item not found: " + itemId);
        }

        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart);
        } else {
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void clearCart() {
        User customer = getCurrentUser();
        cartRepository.findByCustomerId(customer.getId())
                .ifPresent(cartRepository::delete);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    private CartResponseDto toDto(Cart cart) {
        List<CartItemDto> items = cart.getItems().stream()
                .map(ci -> new CartItemDto(
                        ci.getId(),
                        ci.getMenuItem().getId(),
                        ci.getMenuItem().getName(),
                        ci.getQuantity(),
                        ci.getMenuItem().getPrice()
                ))
                .toList();

        BigDecimal total = items.stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDto(
                cart.getId(),
                cart.getRestaurant().getId(),
                cart.getRestaurant().getName(),
                total,
                items
        );
    }
}

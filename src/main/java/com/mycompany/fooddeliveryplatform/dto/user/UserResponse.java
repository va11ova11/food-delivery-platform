package com.mycompany.fooddeliveryplatform.dto.user;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String role
) {}
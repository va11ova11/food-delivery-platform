package com.mycompany.fooddeliveryplatform.controller;

import com.mycompany.fooddeliveryplatform.dto.authDto.JwtResponse;
import com.mycompany.fooddeliveryplatform.dto.authDto.LoginRequest;
import com.mycompany.fooddeliveryplatform.dto.user.RegisterRequest;
import com.mycompany.fooddeliveryplatform.dto.user.UserResponse;
import com.mycompany.fooddeliveryplatform.security.JwtService;
import com.mycompany.fooddeliveryplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        return new JwtResponse(token);
    }

    @GetMapping("/api/auth/me")
    public Map<String, Object> me(org.springframework.security.core.Authentication authentication) {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("name", authentication.getName());
        result.put("authorities", authentication.getAuthorities());
        return result;
    }
}
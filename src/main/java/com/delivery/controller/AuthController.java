package com.delivery.controller;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.LoginRequest;
import com.delivery.dto.UserDto;
import com.delivery.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(201).body(authService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }
}

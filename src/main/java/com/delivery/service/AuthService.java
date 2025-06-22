package com.delivery.service;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.UserRequestDto;

public interface AuthService {
    AuthResponse register(UserRequestDto userDto);
    AuthResponse login(String email, String password);
}

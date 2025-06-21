package com.delivery.service;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.UserDto;

public interface AuthService {
    AuthResponse register(UserDto userDto);
    AuthResponse login(String email, String password);
}

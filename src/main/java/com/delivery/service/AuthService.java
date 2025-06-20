package com.delivery.service;

import com.delivery.dto.UserDto;

public interface AuthService {
    String register(UserDto userDto);
    String login(String email, String password);
}

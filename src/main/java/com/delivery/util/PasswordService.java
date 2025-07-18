package com.delivery.util;

import com.delivery.dto.UserRequestDto;
import com.delivery.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void updatePasswordIfProvided(User user, UserRequestDto userDto) {
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(encodePassword(userDto.getPassword()));
        }
    }
}

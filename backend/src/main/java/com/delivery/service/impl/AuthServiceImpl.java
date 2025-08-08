package com.delivery.service.impl;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.UserRequestDto;
import com.delivery.entity.User;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.service.interfaces.AuthService;
import com.delivery.util.JwtUtil;
import com.delivery.util.validation.RoleValidator;
import com.delivery.util.security.PasswordService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RoleValidator roleValidator;
    private final PasswordService passwordService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           JwtUtil jwtUtil, RoleValidator roleValidator, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.roleValidator = roleValidator;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public AuthResponse register(UserRequestDto userDto) {
        if (userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        roleValidator.validateRegistrationRole(userDto.getRole());

        userDto.setPassword(passwordService.encodePassword(userDto.getPassword()));
        User savedUser = userRepository.save(userMapper.userToEntity(userDto));
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().toString());

        return new AuthResponse(token, "Bearer", savedUser.getRole().toString());
    }

    @Override
    @Transactional
    public AuthResponse login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with this email does not exist"));

        if (!passwordService.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        return new AuthResponse(token, "Bearer", user.getRole().toString());
    }
}
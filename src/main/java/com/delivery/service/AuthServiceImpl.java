package com.delivery.service;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.UserRequestDto;
import com.delivery.entity.User;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.util.JwtUtil;
import com.delivery.util.PasswordService;
import com.delivery.util.RoleValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RoleValidator roleValidator;
    private final PasswordService passwordService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper
            , JwtUtil jwtUtil, RoleValidator roleValidator, PasswordService passwordService) {
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
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponse(token, "Bearer");
    }

    @Override
    @Transactional
    public AuthResponse login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with this email does not exist"));

        if (!passwordService.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, "Bearer");
    }
}

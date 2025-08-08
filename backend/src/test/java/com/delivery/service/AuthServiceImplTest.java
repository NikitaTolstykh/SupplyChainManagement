package com.delivery.service;

import com.delivery.dto.AuthResponse;
import com.delivery.dto.UserRequestDto;
import com.delivery.entity.User;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.service.impl.AuthServiceImpl;
import com.delivery.util.JwtUtil;
import com.delivery.util.Role;
import com.delivery.util.security.PasswordService;
import com.delivery.util.validation.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRequestDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserRequestDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPhone("+1234567890");
        userDto.setRole(Role.CLIENT);

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.CLIENT);
    }

    @Test
    void register_ShouldCreateNewUser_WhenEmailNotExists() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.empty());
        doNothing().when(roleValidator).validateRegistrationRole(Role.CLIENT);
        when(passwordService.encodePassword("password123")).thenReturn("encodedPassword");
        when(userMapper.userToEntity(any(UserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken("test@example.com", user.getRole().toString())).thenReturn("jwt-token");

        AuthResponse response = authService.register(userDto);

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("CLIENT", response.getRole());
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(userDto));

        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordService.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com", user.getRole().toString())).thenReturn("jwt-token");

        AuthResponse response = authService.login("test@example.com", "password123");

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("CLIENT", response.getRole());
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findUserByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authService.login("unknown@example.com", "password"));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordService.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login("test@example.com", "wrongPassword"));
    }
}

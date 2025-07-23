package com.delivery.service;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import com.delivery.event.WorkerCreatedEvent;
import com.delivery.exception.EmailAlreadyExistsException;
import com.delivery.exception.WorkerNotFoundException;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.service.impl.AdminServiceImpl;
import com.delivery.util.Role;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.security.PasswordService;
import com.delivery.util.updateData.UserDataService;
import com.delivery.util.validation.EmailValidationService;
import com.delivery.util.validation.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private RoleValidator roleValidator;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private UserLookupService userLookupService;
    @Mock private EmailValidationService emailValidationService;
    @Mock private PasswordService passwordService;
    @Mock private UserDataService userDataService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private UserRequestDto userDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setRole(Role.DRIVER);
        user.setPassword("encodedPass");

        userDto = new UserRequestDto();
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPhone("1234567890");
        userDto.setRole(Role.DRIVER);
        userDto.setPassword("rawPass");

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setEmail("test@example.com");
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setPhone("1234567890");
        userResponseDto.setRole(Role.DRIVER);
    }

    @Test
    void getAllWorkers_ShouldReturnListOfUsers() {
        when(userRepository.findAllByRoleIn(List.of(Role.DRIVER, Role.DISPATCHER))).thenReturn(List.of(user));
        when(userMapper.userListToResponseDto(List.of(user))).thenReturn(List.of(userResponseDto));

        List<UserResponseDto> result = adminService.getAllWorkers();

        assertEquals(1, result.size());
        verify(userRepository).findAllByRoleIn(List.of(Role.DRIVER, Role.DISPATCHER));
    }

    @Test
    void getAllWorkersByRole_ShouldReturnUsersOfGivenRole() {
        when(userRepository.findAllByRole(Role.DRIVER)).thenReturn(List.of(user));
        when(userMapper.userListToResponseDto(List.of(user))).thenReturn(List.of(userResponseDto));

        List<UserResponseDto> result = adminService.getAllWorkersByRole(Role.DRIVER);

        assertEquals(1, result.size());
        verify(roleValidator).validateRolesForAdmin(Role.DRIVER);
    }
    @Test
    void searchWorkers_ShouldReturnMatchingUsers() {
        String query = "john";
        when(userRepository.findAllByRoleInAndFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                List.of(Role.DRIVER, Role.DISPATCHER), query, query))
                .thenReturn(List.of(user));
        when(userMapper.userListToResponseDto(List.of(user))).thenReturn(List.of(userResponseDto));

        List<UserResponseDto> result = adminService.searchWorkers(query);

        assertEquals(1, result.size());
    }

    @Test
    void getWorker_ShouldReturnUser() {
        when(userLookupService.findUserById(1L)).thenReturn(user);
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.getWorker(1L);

        assertEquals(userResponseDto.getEmail(), result.getEmail());
    }

    @Test
    void addWorker_ShouldAddNewUser() {
        when(passwordService.encodePassword("rawPass")).thenReturn("encodedPass");
        when(userMapper.userToEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.addWorker(userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(roleValidator).validateRolesForAdmin(Role.DRIVER);
        verify(emailValidationService).validateEmailUniqueness(userDto.getEmail());
        verify(passwordService).encodePassword("rawPass");
        verify(eventPublisher).publishEvent(any(WorkerCreatedEvent.class));
    }

    @Test
    void editWorker_ShouldUpdateUser() {
        when(userLookupService.findUserById(1L)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.editWorker(1L, userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userDataService).updateUserFields(user, userDto);
    }

    @Test
    void deleteWorker_ShouldDeleteUser() {
        when(userLookupService.findUserById(1L)).thenReturn(user);

        adminService.deleteWorker(1L);

        verify(userRepository).delete(user);
    }
}
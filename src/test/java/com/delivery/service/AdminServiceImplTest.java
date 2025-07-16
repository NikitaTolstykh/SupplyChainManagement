package com.delivery.service;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import com.delivery.event.WorkerCreatedEvent;
import com.delivery.exception.EmailAlreadyExistsException;
import com.delivery.exception.WorkerNotFoundException;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.util.Role;
import com.delivery.util.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

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
        verify(userRepository).findAllByRoleInAndFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                List.of(Role.DRIVER, Role.DISPATCHER), query, query);
    }

    @Test
    void getWorker_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.getWorker(1L);

        assertEquals(userResponseDto.getEmail(), result.getEmail());
    }

    @Test
    void getWorker_ShouldThrowException_IfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> adminService.getWorker(1L));
    }

    @Test
    void addWorker_ShouldAddNewUser() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");
        when(userMapper.userToEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.addWorker(userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(roleValidator).validateRolesForAdmin(Role.DRIVER);
        verify(eventPublisher).publishEvent(any(WorkerCreatedEvent.class));
    }

    @Test
    void addWorker_ShouldThrowException_IfEmailExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> adminService.addWorker(userDto));
    }

    @Test
    void editWorker_ShouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = adminService.editWorker(1L, userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(roleValidator).validateRolesForAdmin(Role.DRIVER);
    }

    @Test
    void editWorker_ShouldThrowException_IfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> adminService.editWorker(1L, userDto));
    }

    @Test
    void editWorker_ShouldThrowException_IfEmailAlreadyExists() {
        User existing = new User();
        existing.setId(1L);
        existing.setEmail("old@example.com");

        userDto.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> adminService.editWorker(1L, userDto));
    }

    @Test
    void deleteWorker_ShouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        adminService.deleteWorker(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteWorker_ShouldThrowException_IfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> adminService.deleteWorker(1L));
    }


}

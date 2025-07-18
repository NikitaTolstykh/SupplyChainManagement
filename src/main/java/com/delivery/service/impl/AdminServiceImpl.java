package com.delivery.service.impl;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import com.delivery.event.WorkerCreatedEvent;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.service.interfaces.AdminService;
import com.delivery.util.*;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.security.PasswordService;
import com.delivery.util.updateData.UserDataService;
import com.delivery.util.validation.EmailValidationService;
import com.delivery.util.validation.RoleValidator;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleValidator roleValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final UserLookupService userLookupService;
    private final EmailValidationService emailValidationService;
    private final PasswordService passwordService;
    private final UserDataService userDataService;

    public AdminServiceImpl(UserRepository userRepository, UserMapper userMapper,
                            RoleValidator roleValidator, ApplicationEventPublisher eventPublisher,
                            UserLookupService userLookupService, EmailValidationService emailValidationService,
                            PasswordService passwordService, UserDataService userDataService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleValidator = roleValidator;
        this.eventPublisher = eventPublisher;
        this.userLookupService = userLookupService;
        this.emailValidationService = emailValidationService;
        this.passwordService = passwordService;
        this.userDataService = userDataService;
    }

    @Override
    @Transactional
    @Cacheable(value = "get-all-workers")
    public List<UserResponseDto> getAllWorkers() {
        return userMapper.userListToResponseDto(
                userRepository.findAllByRoleIn(List.of(Role.DRIVER, Role.DISPATCHER))
        );

    }

    @Override
    @Transactional
    public List<UserResponseDto> getAllWorkersByRole(Role role) {
        roleValidator.validateRolesForAdmin(role);
        return userMapper.userListToResponseDto(userRepository.findAllByRole(role));
    }

    @Override
    @Transactional
    @Cacheable(value = "search-workers", key = "#query")
    public List<UserResponseDto> searchWorkers(String query) {
        return userMapper.userListToResponseDto(
                userRepository.findAllByRoleInAndFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        List.of(Role.DRIVER, Role.DISPATCHER),
                        query,
                        query
                )
        );
    }

    @Override
    @Transactional
    public UserResponseDto getWorker(Long id) {
        User worker = userLookupService.findUserById(id);
        return userMapper.userToResponseDto(worker);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"get-all-workers", "search-workers"}, allEntries = true)
    public UserResponseDto addWorker(UserRequestDto userDto) {
        roleValidator.validateRolesForAdmin(userDto.getRole());

        emailValidationService.validateEmailUniqueness(userDto.getEmail());
        userDto.setPassword(passwordService.encodePassword(userDto.getPassword()));

        User newWorker = userMapper.userToEntity(userDto);
        User savedWorker = userRepository.save(newWorker);

        String password = userDto.getPassword();
        eventPublisher.publishEvent(new WorkerCreatedEvent(savedWorker, password));

        return userMapper.userToResponseDto(savedWorker);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"get-all-workers", "search-workers"}, allEntries = true)
    public UserResponseDto editWorker(Long id, UserRequestDto userDto) {
        User workerToEdit = userLookupService.findUserById(id);
        roleValidator.validateRolesForAdmin(userDto.getRole());

        emailValidationService.validateEmailForUpdate(workerToEdit, userDto.getEmail());

        userDataService.updateUserFields(workerToEdit, userDto);

        return userMapper.userToResponseDto(userRepository.save(workerToEdit));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"get-all-workers", "search-workers"}, allEntries = true)
    public void deleteWorker(Long id) {
        User worker = userLookupService.findUserById(id);
        userRepository.delete(worker);

    }
}

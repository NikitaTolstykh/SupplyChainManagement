package com.delivery.service;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import com.delivery.exception.EmailAlreadyExistsException;
import com.delivery.exception.WorkerNotFoundException;
import com.delivery.mapper.UserMapper;
import com.delivery.repository.UserRepository;
import com.delivery.util.Role;
import com.delivery.util.RoleValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleValidator roleValidator;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            UserMapper userMapper, RoleValidator roleValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleValidator = roleValidator;
    }

    @Override
    @Transactional
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
        User worker = findUserById(id);
        return userMapper.userToResponseDto(worker);
    }

    @Override
    @Transactional
    public UserResponseDto addWorker(UserRequestDto userDto) {
        roleValidator.validateRolesForAdmin(userDto.getRole());

        validateIfEmailExists(userDto.getEmail());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User newWorker = userMapper.userToEntity(userDto);
        return userMapper.userToResponseDto(userRepository.save(newWorker));
    }

    @Override
    @Transactional
    public UserResponseDto editWorker(Long id, UserRequestDto userDto) {
        User workerToEdit = findUserById(id);
        roleValidator.validateRolesForAdmin(userDto.getRole());

        validateEmailForUpdate(workerToEdit, userDto.getEmail());

        updateWorkerData(workerToEdit, userDto);

        return userMapper.userToResponseDto(userRepository.save(workerToEdit));
    }

    @Override
    @Transactional
    public void deleteWorker(Long id) {
        userRepository.delete(findUserById(id));

    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException("Worker with id: " + id + " not found"));
    }

    private void validateIfEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("This email already exists");
        }
    }

    private void validateEmailForUpdate(User workerToEdit, String email) {
        if (!workerToEdit.getEmail().equals(email)) {
            validateIfEmailExists(email);
        }
    }

    private void checkPasswordBeforeEditing(User user, UserRequestDto userDto){
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
    }


    private void updateWorkerData(User user, UserRequestDto userDto) {
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        checkPasswordBeforeEditing(user, userDto);
    }

}

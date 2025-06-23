package com.delivery.service;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.util.Role;

import java.util.List;

public interface AdminService {
    List<UserResponseDto> getAllWorkers();

    List<UserResponseDto> getAllWorkersByRole(Role role);

    List<UserResponseDto> searchWorkers(String query);

    UserResponseDto getWorker(Long id);

    UserResponseDto addWorker(UserRequestDto userDto);

    UserResponseDto editWorker(Long id, UserRequestDto userDto);

    void deleteWorker(Long id);

}

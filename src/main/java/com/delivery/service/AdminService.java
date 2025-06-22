package com.delivery.service;

import com.delivery.dto.UserDto;
import com.delivery.dto.UserResponseDto;

import java.util.List;

public interface AdminService {
    List<UserResponseDto> getAllWorkers();
    UserResponseDto getWorker(Long id);
    UserResponseDto addWorker(UserDto userDto);
    UserResponseDto editWorker(Long id, UserDto userDto);
    void deleteWorker(Long id);

}

package com.delivery.service;

import com.delivery.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllWorkers();
    UserDto getWorker(Long id);
    UserDto addWorker(UserDto userDto);
    UserDto editWorker(Long id, UserDto userDto);
    void deleteWorker(Long id);

}

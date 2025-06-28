package com.delivery.mapper;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToResponseDto(User user);

    List<UserResponseDto> userListToResponseDto(List<User> users);

    UserRequestDto userToDto(User user);

    User userToEntity(UserRequestDto userDto);
}

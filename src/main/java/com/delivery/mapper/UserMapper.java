package com.delivery.mapper;

import com.delivery.dto.UserDto;
import com.delivery.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToDto(User user);
    User userToEntity(UserDto userDto);
}

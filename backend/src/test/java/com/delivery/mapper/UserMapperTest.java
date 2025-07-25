package com.delivery.mapper;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.entity.User;
import com.delivery.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User user;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+1234567890");
        user.setPassword("encodedPass");
        user.setRole(Role.CLIENT);

        userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("john@example.com");
        userRequestDto.setFirstName("John");
        userRequestDto.setLastName("Doe");
        userRequestDto.setPhone("+1234567890");
        userRequestDto.setPassword("rawPass");
        userRequestDto.setRole(Role.CLIENT);
    }

    @Test
    void userToResponseDto_ShouldMapCorrectly() {
        UserResponseDto dto = userMapper.userToResponseDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getPhone(), dto.getPhone());
        assertEquals(user.getRole(), dto.getRole());
    }

    @Test
    void userListToResponseDto_ShouldMapListCorrectly() {
        List<UserResponseDto> dtoList = userMapper.userListToResponseDto(List.of(user));

        assertEquals(1, dtoList.size());
        assertEquals(user.getEmail(), dtoList.get(0).getEmail());
    }

    @Test
    void userToEntity_ShouldMapCorrectly() {
        User entity = userMapper.userToEntity(userRequestDto);

        assertNull(entity.getId()); // not mapped from DTO
        assertEquals(userRequestDto.getEmail(), entity.getEmail());
        assertEquals(userRequestDto.getFirstName(), entity.getFirstName());
        assertEquals(userRequestDto.getLastName(), entity.getLastName());
        assertEquals(userRequestDto.getPhone(), entity.getPhone());
        assertEquals(userRequestDto.getPassword(), entity.getPassword());
        assertEquals(userRequestDto.getRole(), entity.getRole());
    }
}

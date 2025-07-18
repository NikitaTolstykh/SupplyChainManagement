package com.delivery.util.changeData;

import com.delivery.dto.UserRequestDto;
import com.delivery.entity.User;
import com.delivery.util.security.PasswordService;
import org.springframework.stereotype.Component;

@Component
public class UserDataService {
    private final PasswordService passwordService;

    public UserDataService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public void updateUserFields(User user, UserRequestDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        passwordService.updatePasswordIfProvided(user, userDto);
    }
}
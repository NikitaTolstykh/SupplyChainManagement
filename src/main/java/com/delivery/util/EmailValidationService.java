package com.delivery.util;

import com.delivery.entity.User;
import com.delivery.exception.EmailAlreadyExistsException;
import com.delivery.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationService {
    private final UserRepository userRepository;

    public EmailValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("This email already exists");
        }
    }

    public void validateEmailForUpdate(User user, String newEmail) {
        if (!user.getEmail().equals(newEmail)) {
            validateEmailUniqueness(newEmail);
        }
    }
}
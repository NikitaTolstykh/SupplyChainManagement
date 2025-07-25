package com.delivery.util.lookup;

import com.delivery.entity.User;
import com.delivery.exception.UserWithEmailNotFoundException;
import com.delivery.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserLookupService {
    private final UserRepository userRepository;

    public UserLookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException("User with email: " + email + " not found"));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserWithEmailNotFoundException("User with id: " + id + " not found"));
    }

}

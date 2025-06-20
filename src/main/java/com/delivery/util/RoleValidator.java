package com.delivery.util;

import com.delivery.exception.RoleNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {
    public void validateRegistrationRole(Role role) {
        if (role != Role.CLIENT) {
            throw new RoleNotAllowedException("Only CLIENT users can register");
        }
    }
}

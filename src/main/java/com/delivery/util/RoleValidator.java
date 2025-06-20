package com.delivery.util;

import com.delivery.exception.RoleNotAllowedException;

public class RoleValidator {
    public void validateRegistrationRole(Role role) {
        if (role != Role.CLIENT) {
            throw new RoleNotAllowedException("Only CLIENT users can register");
        }
    }
}

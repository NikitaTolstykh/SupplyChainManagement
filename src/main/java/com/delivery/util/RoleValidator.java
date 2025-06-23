package com.delivery.util;

import com.delivery.exception.InvalidDriverRoleException;
import com.delivery.exception.RoleNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {
    public void validateRegistrationRole(Role role) {
        if (role != Role.CLIENT) {
            throw new RoleNotAllowedException("Only CLIENT users can register");
        }
    }

    public void validateDriverRole(Role role) {
        if (role != Role.DRIVER) {
            throw new InvalidDriverRoleException("Assigned user must be a DRIVER");
        }
    }

    public void validateRolesForAdmin(Role role) {
        if (role != Role.DRIVER && role != Role.DISPATCHER) {
            throw new RoleNotAllowedException("Admin can have access only to workers");
        }
    }
}

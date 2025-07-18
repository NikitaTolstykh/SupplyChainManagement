package com.delivery.util.validation;

import com.delivery.entity.Order;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class AccessValidationService {

    public void validateOrderAccess(Order order, String userEmail) {
        if (order.getClient() == null || !order.getClient().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("Access denied to this order");
        }
    }

    public void validateDriverAccess(Order order, String driverEmail) {
        if (order.getDriver() == null || !order.getDriver().getEmail().equals(driverEmail)) {
            try {
                throw new AccessDeniedException("You are not allowed to access this order");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

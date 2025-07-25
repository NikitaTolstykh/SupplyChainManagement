package com.delivery.exception;

public class RoleNotAllowedException extends RuntimeException{
    public RoleNotAllowedException(String message) {
        super(message);
    }
}

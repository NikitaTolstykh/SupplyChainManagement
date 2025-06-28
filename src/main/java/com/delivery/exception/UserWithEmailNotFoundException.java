package com.delivery.exception;

public class UserWithEmailNotFoundException extends RuntimeException {
    public UserWithEmailNotFoundException(String message) {
        super(message);
    }
}

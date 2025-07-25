package com.delivery.exception;

public class LicensePlateAlreadyExistsException extends RuntimeException {
    public LicensePlateAlreadyExistsException(String message) {
        super(message);
    }
}

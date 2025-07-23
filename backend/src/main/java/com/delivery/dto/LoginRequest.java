package com.delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email must be valid")
    @NotBlank(message = "user must have an email")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 symbols")
    @NotBlank(message = "user must have a password")
    private String password;
}

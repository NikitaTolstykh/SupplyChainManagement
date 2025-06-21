package com.delivery.dto;

import com.delivery.util.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @Email(message = "Email must be valid")
    @NotBlank(message = "user must have an email")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 symbols")
    @NotBlank(message = "user must have a password")
    private String password;

    @NotBlank(message = "user must have a firstName")
    private String firstName;

    @NotBlank(message = "user must have a lastName")
    private String lastName;

    @Pattern(  regexp = "^(\\+\\d{1,3})?\\d{10,15}$",
            message = "Phone number must me valid")
    @NotBlank(message = "user must have phone number")
    private String phone;

    @NotNull(message = "user must have a role")
    private Role role;

}

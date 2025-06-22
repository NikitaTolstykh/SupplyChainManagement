package com.delivery.dto;

import com.delivery.util.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;

    @Email(message = "Email must be valid")
    @NotBlank(message = "user must have an email")
    private String email;

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

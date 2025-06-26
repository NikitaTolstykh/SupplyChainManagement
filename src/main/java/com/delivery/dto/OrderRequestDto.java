package com.delivery.dto;

import com.delivery.util.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotBlank(message = "Sender's address is required")
    @Size(max = 500, message = "Sender's address must be less than 500 characters")
    private String fromAddress;

    @NotBlank(message = "Destination address is required")
    @Size(max = 500, message = "Destination address must be less than 500 characters")
    private String toAddress;

    @NotBlank(message = "Cargo type is required")
    @Size(max = 100, message = "Cargo type must be less than 100 characters")
    private String cargoType;

    @Size(max = 1000, message = "Cargo description must be less than 1000 characters")
    private String cargoDescription;

    @NotNull(message = "Cargo weight is required")
    @DecimalMin(value = "0.1", message = "Weight must be more than 0")
    @DecimalMax(value = "50000.0", message = "Weight must be less than 50000 kg")
    @Digits(integer = 8, fraction = 2, message = "Invalid weight format")
    private BigDecimal weightKg;

    private String comment;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Future(message = "Pickup time must be in the future")
    private LocalDateTime pickupTime;
}

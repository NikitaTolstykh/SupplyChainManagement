package com.delivery.dto;

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
public class OrderCreatedEmailDto {
    private Long orderId;
    private String clientFirstName;
    private String clientLastName;
    private String clientEmail;
    private String fromAddress;
    private String toAddress;
    private String cargoType;
    private String cargoDescription;
    private BigDecimal weightKg;
    private BigDecimal price;
    private String paymentMethod;
    private LocalDateTime pickupTime;
    private LocalDateTime createdAt;
    private String comment;
}

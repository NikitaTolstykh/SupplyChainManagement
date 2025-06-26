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
public class OrderRequestDto {
    private String fromAddress;
    private String toAddress;
    private String cargoType;
    private String cargoDescription;
    private BigDecimal weightKg;
    private String comment;
    private LocalDateTime pickupTime;
}

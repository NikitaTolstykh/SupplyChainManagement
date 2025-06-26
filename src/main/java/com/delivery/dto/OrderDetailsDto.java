package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {
    private Long id;
    private String fromAddress;
    private String toAddress;
    private String cargoType;
    private String cargoDescription;
    private BigDecimal weightKg;
    private String comment;
    private BigDecimal price;
    private String status;
    private LocalDateTime pickupTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentMethod;
    private RouteInfoDto routeInfoDto;

}

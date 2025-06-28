package com.delivery.dto;

import com.delivery.util.OrderStatus;
import com.delivery.util.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DispatcherOrderDetailsDto {
    private Long id;
    private String fromAddress;
    private String toAddress;
    private String cargoType;
    private String cargoDescription;
    private BigDecimal weightKg;
    private String comment;
    private BigDecimal price;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private LocalDateTime pickupTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhoneNumber;

    private Long driverId;
    private String driverName;

    private Long vehicleId;
    private String licensePlate;
}

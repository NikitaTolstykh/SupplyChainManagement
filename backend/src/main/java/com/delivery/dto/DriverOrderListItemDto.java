package com.delivery.dto;

import com.delivery.util.OrderStatus;
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
public class DriverOrderListItemDto {
    private Long id;
    private String fromAddress;
    private String toAddress;
    private OrderStatus status;
    private LocalDateTime pickupTime;
    private BigDecimal price;
}

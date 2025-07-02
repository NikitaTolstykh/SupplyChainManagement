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
@NoArgsConstructor
@AllArgsConstructor
public class OrderListItemDto {
    private Long id;
    private String fromAddress;
    private String status;
    private BigDecimal price;
    private LocalDateTime createdAt;
}

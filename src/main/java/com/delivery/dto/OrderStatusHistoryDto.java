package com.delivery.dto;

import com.delivery.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryDto {
    private Long id;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private LocalDateTime changedAt;
}

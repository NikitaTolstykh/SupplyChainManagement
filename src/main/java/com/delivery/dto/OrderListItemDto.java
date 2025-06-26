package com.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderListItemDto {
    private Long id;
    private String fromAddress;
    private String status;
    private BigDecimal price;
    private LocalDateTime createdAt;

}

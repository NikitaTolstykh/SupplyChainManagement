package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatisticsDto {
    private Long totalOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private BigDecimal totalSpent;
    private BigDecimal averageOrderValue;
    private Map<String, Long> ordersByMonth;
    private Map<String, BigDecimal> spentByMonth;
    private double averageRating;
    private String mostUsedRoute;

    public static ClientStatisticsDto empty() {
        return new ClientStatisticsDto(
                0L, 0L, 0L,
                BigDecimal.ZERO, BigDecimal.ZERO,
                new HashMap<>(), new HashMap<>(),
                0.0, null
        );
    }
}

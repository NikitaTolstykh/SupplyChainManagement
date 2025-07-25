package com.delivery.dto.projection;

import java.math.BigDecimal;

public interface MonthlyStatsProjection {
    String getYearMonth();

    Long getOrderCount();

    BigDecimal getTotalAmount();
}

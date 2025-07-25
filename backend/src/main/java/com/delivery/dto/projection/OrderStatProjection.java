package com.delivery.dto.projection;

import java.math.BigDecimal;

public interface OrderStatProjection {
    Long getTotalOrders();

    Long getCompletedOrders();

    Long getCancelledOrders();

    BigDecimal getTotalAmount();
}

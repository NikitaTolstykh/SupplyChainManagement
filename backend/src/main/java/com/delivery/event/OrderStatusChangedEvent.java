package com.delivery.event;

import com.delivery.entity.Order;
import com.delivery.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusChangedEvent {
    private Order order;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
}

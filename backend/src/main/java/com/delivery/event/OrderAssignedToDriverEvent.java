package com.delivery.event;

import com.delivery.entity.Order;
import com.delivery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderAssignedToDriverEvent {
    private Order order;
    private User driver;
}

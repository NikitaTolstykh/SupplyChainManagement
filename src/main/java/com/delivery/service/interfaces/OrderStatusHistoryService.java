package com.delivery.service.interfaces;

import com.delivery.entity.Order;
import com.delivery.entity.OrderStatusHistory;
import com.delivery.entity.User;
import com.delivery.util.OrderStatus;

import java.util.List;

public interface OrderStatusHistoryService {

    void logStatusChange(Order order, OrderStatus from, OrderStatus to, User changedBy);

    List<OrderStatusHistory> getOrderHistory(Long orderId);
}

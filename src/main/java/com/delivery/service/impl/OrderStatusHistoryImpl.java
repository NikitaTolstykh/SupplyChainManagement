package com.delivery.service.impl;

import com.delivery.entity.Order;
import com.delivery.entity.OrderStatusHistory;
import com.delivery.entity.User;
import com.delivery.repository.OrderStatusHistoryRepository;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderStatusHistoryImpl implements OrderStatusHistoryService {
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public OrderStatusHistoryImpl(OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    @Override
    @Transactional
    public void logStatusChange(Order order, OrderStatus from, OrderStatus to, User changedBy) {
        if (from == to) return;

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(from);
        history.setToStatus(to);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());

        orderStatusHistoryRepository.save(history);
    }

    @Override
    public List<OrderStatusHistory> getOrderHistory(Long orderId) {
        return orderStatusHistoryRepository.findByOrder_IdOrderByChangedAtDesc(orderId);
    }
}

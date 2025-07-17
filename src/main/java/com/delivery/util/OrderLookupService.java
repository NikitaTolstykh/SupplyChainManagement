package com.delivery.util;

import com.delivery.entity.Order;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderLookupService {

    private final OrderRepository orderRepository;

    public OrderLookupService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not found"));
    }

    public boolean existsById(Long orderId) {
        return orderRepository.existsById(orderId);
    }
}

package com.delivery.repository;

import com.delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByClient_Email(String clientEmail);
    Order findAllByClient_Email(String clientEmail);

}

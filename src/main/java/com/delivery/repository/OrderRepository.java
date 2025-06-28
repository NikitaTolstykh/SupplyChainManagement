package com.delivery.repository;

import com.delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByClient_Email(String clientEmail);
    List<Order> findAllByClient_Email(String clientEmail);

}

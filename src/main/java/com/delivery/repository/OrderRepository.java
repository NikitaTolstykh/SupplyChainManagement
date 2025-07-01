package com.delivery.repository;

import com.delivery.entity.Order;
import com.delivery.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClient_Email(String clientEmail);

    List<Order> findAllByDriver_IdAndStatus(Long driverId, OrderStatus status);

}

package com.delivery.repository;

import com.delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByClient_Email(String clientEmail);

    List<Order> findAllByClient_Email(String clientEmail);

}

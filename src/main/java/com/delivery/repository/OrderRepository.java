package com.delivery.repository;

import com.delivery.entity.Order;
import com.delivery.util.OrderStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClient_Email(String clientEmail);

    List<Order> findAllByDriver_IdAndStatus(Long driverId, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.client.email = :email AND o.status = 'DELIVERED'" +
            " AND o.id NOT IN (SELECT r.order.id FROM OrderRating r)")
    List<Order> findOrdersEligibleForRatingByClientEmail(@Param("email") String email);

}

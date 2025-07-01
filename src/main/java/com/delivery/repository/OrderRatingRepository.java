package com.delivery.repository;

import com.delivery.entity.Order;
import com.delivery.entity.OrderRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating, Long> {
    boolean existsByOrder(Order order);

    Optional<OrderRating> findByOrder(Order order);


}

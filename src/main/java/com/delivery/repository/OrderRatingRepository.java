package com.delivery.repository;

import com.delivery.dto.projection.RatingStatsProjection;
import com.delivery.entity.Order;
import com.delivery.entity.OrderRating;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating, Long> {

    @Query("""
            SELECT COALESCE(AVG (CAST(r.stars AS DOUBLE)), 0.0) as averageRating
                        FROM OrderRating r WHERE r.client.email = :clientEmail
            """)
    RatingStatsProjection getRatingStatsByClientEmail(@Param("clientEmail") String clientEmail);
}

package com.delivery.repository;

import com.delivery.dto.projection.MonthlyStatsProjection;
import com.delivery.dto.projection.OrderStatProjection;
import com.delivery.dto.projection.RouteStatsProjection;
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

    @Query("""
            SELECT COUNT(o) as totalOrders,
                   SUM(CASE WHEN o.status = 'DELIVERED' THEN 1 ELSE 0 END) as completedOrders,
                   SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelledOrders,
                   COALESCE(SUM(o.price), 0) as totalAmount
            FROM Order o WHERE o.client.email = :clientEmail
            """)
    OrderStatProjection getOrderStatsByClientEmail(@Param("clientEmail") String clientEmail);





    @Query("""
            SELECT CONCAT(o.fromAddress, ' -> ', o.toAddress) as route,
                       COUNT (o) as orderCount
                       FROM Order o
                                  WHERE o.client.email =:clientEmail
                                  GROUP BY o.fromAddress, o.toAddress
                                  ORDER BY orderCount DESC 
            """)
    List<RouteStatsProjection> getRouteStatsByClientEmail(@Param("clientEmail") String clientEmail);


}

package com.delivery.mapper;

import com.delivery.dto.ClientStatisticsDto;
import com.delivery.dto.projection.MonthlyStatsProjection;
import com.delivery.dto.projection.OrderStatProjection;
import com.delivery.dto.projection.RatingStatsProjection;
import com.delivery.dto.projection.RouteStatsProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "Spring")
public interface ClientStatisticsMapper {
    @Mapping(source = "orderStats.totalOrders", target = "totalOrders")
    @Mapping(source = "orderStats.completedOrders", target = "completedOrders")
    @Mapping(source = "orderStats.cancelledOrders", target = "cancelledOrders")
    @Mapping(source = "orderStats.totalAmount", target = "totalSpent")
    @Mapping(source = "orderStats", target = "averageOrderValue", qualifiedByName = "calculateAverageOrderValue")
    @Mapping(source = "monthlyStats", target = "ordersByMonth", qualifiedByName = "convertToOrdersByMonth")
    @Mapping(source = "monthlyStats", target = "spentByMonth", qualifiedByName = "convertToSpentByMonth")
    @Mapping(source = "ratingStats.averageRating", target = "averageRating")
    @Mapping(source = "routeStats", target = "mostUsedRoute", qualifiedByName = "getMostUsedRoute")
    ClientStatisticsDto toClientStatisticDto(OrderStatProjection orderStats,
                                             RatingStatsProjection ratingStats,
                                             List<MonthlyStatsProjection> monthlyStats,
                                             List<RouteStatsProjection> routeStats);


    @Named("calculateAverageOrderValue")
    default BigDecimal calculateAverageOrderValue(OrderStatProjection orderStats) {
        if (orderStats == null || orderStats.getTotalOrders() == null || orderStats.getTotalOrders() == 0
                || orderStats.getTotalAmount() == null) {
            return BigDecimal.ZERO;
        }
        return orderStats.getTotalAmount().divide(BigDecimal.valueOf(orderStats.getTotalOrders()), 2, RoundingMode.HALF_UP);
    }
    @Named("convertToSpentByMonth")
    default Map<String, BigDecimal> convertToSpentByMonth(List<MonthlyStatsProjection> monthlyStats) {
        if (monthlyStats == null || monthlyStats.isEmpty()) {
            return Map.of();
        }
        return monthlyStats.stream()
                .collect(Collectors.toMap(
                        MonthlyStatsProjection::getYearMonth,
                        MonthlyStatsProjection::getTotalAmount,
                        (existing, replacement) -> existing
                ));
    }


}

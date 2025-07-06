package com.delivery.service;

import com.delivery.dto.ClientStatisticsDto;
import com.delivery.dto.projection.MonthlyStatsProjection;
import com.delivery.dto.projection.OrderStatProjection;
import com.delivery.dto.projection.RatingStatsProjection;
import com.delivery.dto.projection.RouteStatsProjection;
import com.delivery.mapper.ClientStatisticsMapper;
import com.delivery.repository.OrderRatingRepository;
import com.delivery.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientStatisticsServiceImpl implements ClientStatisticsService {
    private final OrderRepository orderRepository;
    private final OrderRatingRepository orderRatingRepository;
    private final ClientStatisticsMapper clientStatisticsMapper;

    public ClientStatisticsServiceImpl(OrderRepository orderRepository, OrderRatingRepository orderRatingRepository, ClientStatisticsMapper clientStatisticsMapper) {
        this.orderRepository = orderRepository;
        this.orderRatingRepository = orderRatingRepository;
        this.clientStatisticsMapper = clientStatisticsMapper;
    }


    @Override
    public ClientStatisticsDto getClientStatistics(String clientEmail) {
        try {
            OrderStatProjection orderStats = orderRepository.getOrderStatsByClientEmail(clientEmail);

            if (orderStats == null || orderStats.getTotalOrders() == null || orderStats.getTotalOrders() == 0) {
                return ClientStatisticsDto.empty();
            }

            RatingStatsProjection ratingStats = orderRatingRepository.getRatingStatsByClientEmail(clientEmail);

            List<MonthlyStatsProjection> monthlyStats = orderRepository.getMonthlyStatsByClientEmail(clientEmail);

            List<RouteStatsProjection> routeStats = orderRepository.getRouteStatsByClientEmail(clientEmail);

            return clientStatisticsMapper.toClientStatisticDto(orderStats, ratingStats, monthlyStats, routeStats);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch client statistics for email: " + clientEmail, e);
        }
    }
}

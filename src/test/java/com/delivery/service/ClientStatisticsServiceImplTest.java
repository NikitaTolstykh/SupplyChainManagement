package com.delivery.service;

import com.delivery.dto.ClientStatisticsDto;
import com.delivery.dto.projection.MonthlyStatsProjection;
import com.delivery.dto.projection.OrderStatProjection;
import com.delivery.dto.projection.RatingStatsProjection;
import com.delivery.dto.projection.RouteStatsProjection;
import com.delivery.mapper.ClientStatisticsMapper;
import com.delivery.repository.OrderRatingRepository;
import com.delivery.repository.OrderRepository;
import com.delivery.service.ClientStatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientStatisticsServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRatingRepository orderRatingRepository;

    @Mock
    private ClientStatisticsMapper clientStatisticsMapper;

    @InjectMocks
    private ClientStatisticsServiceImpl clientStatisticsService;

    private final String clientEmail = "client@example.com";

    @Test
    void getClientStatistics_shouldReturnClientStatistics() {
        OrderStatProjection orderStats = mock(OrderStatProjection.class);
        when(orderStats.getTotalOrders()).thenReturn(5L);
        RatingStatsProjection ratingStats = mock(RatingStatsProjection.class);
        List<MonthlyStatsProjection> monthlyStats = List.of();
        List<RouteStatsProjection> routeStats = List.of();
        ClientStatisticsDto expected = new ClientStatisticsDto(
                5L, 3L, 2L,
                BigDecimal.valueOf(200), BigDecimal.valueOf(40),
                Map.of(), Map.of(), 4.2, "A->B"
        );

        when(orderRepository.getOrderStatsByClientEmail(clientEmail)).thenReturn(orderStats);
        when(orderRatingRepository.getRatingStatsByClientEmail(clientEmail)).thenReturn(ratingStats);
        when(orderRepository.getMonthlyStatsByClientEmail(clientEmail)).thenReturn(monthlyStats);
        when(orderRepository.getRouteStatsByClientEmail(clientEmail)).thenReturn(routeStats);
        when(clientStatisticsMapper.toClientStatisticDto(orderStats, ratingStats, monthlyStats, routeStats))
                .thenReturn(expected);

        ClientStatisticsDto actual = clientStatisticsService.getClientStatistics(clientEmail);

        assertEquals(expected, actual);
        verify(orderRepository).getOrderStatsByClientEmail(clientEmail);
        verify(orderRatingRepository).getRatingStatsByClientEmail(clientEmail);
        verify(orderRepository).getMonthlyStatsByClientEmail(clientEmail);
        verify(orderRepository).getRouteStatsByClientEmail(clientEmail);
        verify(clientStatisticsMapper).toClientStatisticDto(orderStats, ratingStats, monthlyStats, routeStats);
    }

    @Test
    void getClientStatistics_shouldReturnEmptyStatistics_whenNoOrders() {
        when(orderRepository.getOrderStatsByClientEmail(clientEmail)).thenReturn(null);

        ClientStatisticsDto result = clientStatisticsService.getClientStatistics(clientEmail);

        assertNotNull(result);
        assertEquals(ClientStatisticsDto.empty(), result);

        verify(orderRepository).getOrderStatsByClientEmail(clientEmail);
        verifyNoMoreInteractions(orderRatingRepository, clientStatisticsMapper);
    }
}


package com.delivery.service.interfaces;

import com.delivery.dto.OrderRatingRequestDto;
import com.delivery.dto.OrderRatingResponseDto;

import java.util.List;

public interface OrderRatingService {
    OrderRatingResponseDto rateOrder(Long orderId, OrderRatingRequestDto dto, String clientEmail);

    List<OrderRatingResponseDto> opinionList();
}

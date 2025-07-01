package com.delivery.service;

import com.delivery.dto.OrderRatingRequestDto;
import com.delivery.dto.OrderRatingResponseDto;
import com.delivery.dto.OrderRequestDto;

public interface OrderRatingService {
    OrderRatingResponseDto rateOrder(Long orderId, OrderRatingRequestDto dto, String clientEmail);
    OrderRatingResponseDto getRating(Long orderId, String clientEmail);
}

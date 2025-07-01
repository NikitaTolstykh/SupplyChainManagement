package com.delivery.service;

import com.delivery.dto.OrderRatingResponseDto;
import com.delivery.dto.OrderRequestDto;

public interface OrderRatingService {
    OrderRatingResponseDto createRating(Long orderId, OrderRequestDto dto);
    OrderRatingResponseDto getRating(Long orderId, String clientEmail);
}

package com.delivery.service;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;

import java.util.List;

public interface ClientService {
    OrderDetailsDto createOrder(OrderRequestDto dto, String email);

    List<OrderListItemDto> getClientOrders(String email);

    OrderDetailsDto getOrderDetails(Long orderId, String email);

    List<OrderListItemDto> getOrdersEligibleForRating(String email);
}

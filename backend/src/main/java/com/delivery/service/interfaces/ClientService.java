package com.delivery.service.interfaces;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.dto.OrderStatusHistoryDto;

import java.util.List;

public interface ClientService {
    OrderDetailsDto createOrder(OrderRequestDto dto, String email);

    List<OrderListItemDto> getClientOrders(String email);

    OrderDetailsDto getOrderDetails(Long orderId, String email);

    List<OrderListItemDto> getOrdersAvailableForRating(String email);

    List<OrderStatusHistoryDto> getOrderStatusHistory(Long orderId, String email);

}

package com.delivery.service;

import com.delivery.dto.*;

import java.util.List;

public interface DispatcherService {
    List<OrderListItemDto> getAllOrders();

    DispatcherOrderDetailsDto getOrderDetails(Long id);

    void assignDriver(Long id, AssignDriverRequestDto dto);

    void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto);

    void updateOrderInfo(Long id, OrderRequestDto dto);

    void deleteOrder(Long id);

    List<AvailableDriverDto> availableDrivers();
}

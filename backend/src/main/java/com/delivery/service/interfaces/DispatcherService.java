package com.delivery.service.interfaces;

import com.delivery.dto.*;

import java.util.List;

public interface DispatcherService {
    List<OrderListItemDto> getAllOrders();

    DispatcherOrderDetailsDto getOrderDetails(Long id);

    void assignDriver(Long id, AssignDriverRequestDto dto);

    void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto);

    void updateOrderInfo(Long id, OrderRequestDto dto);

    void cancelOrder(Long id);

    List<AvailableDriverDto> availableDrivers();

    List<OrderStatusHistoryDto> getOrderStatusHistory(Long orderId);
}

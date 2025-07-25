package com.delivery.service.interfaces;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;

import java.util.List;

public interface DriverService {
    List<DriverOrderListItemDto> getAssignedOrders(String driverEmail);

    DispatcherOrderDetailsDto getOrderDetails(Long orderId, String driverEmail);

    void acceptOrder(Long orderId, String driverEmail);

    void completeOrder(Long orderId, String driverEmail);

    List<DriverOrderListItemDto> getCompletedOrders(String driverEmail);

}

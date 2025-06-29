package com.delivery.service;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.DriverMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class DriverServiceImpl implements DriverService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DriverMapper driverMapper;
    private final DispatcherMapper dispatcherMapper;

    public DriverServiceImpl(OrderRepository orderRepository, UserRepository userRepository
            , DriverMapper driverMapper, DispatcherMapper dispatcherMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.driverMapper = driverMapper;
        this.dispatcherMapper = dispatcherMapper;
    }

    @Override
    public List<DriverOrderListItemDto> getAssignedOrders(String driverEmail) {

    }

    @Override
    public DispatcherOrderDetailsDto getOrderDetails(Long orderId, String driverEmail) {

    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, String driverEmail) {

    }

    @Override
    @Transactional
    public void completeOrder(Long orderId, String driverEmail) {

    }

    @Override
    public List<DriverOrderListItemDto> getCompletedOrders(String driverEmail) {

    }
}

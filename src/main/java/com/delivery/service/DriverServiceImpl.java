package com.delivery.service;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.exception.UserWithEmailNotFoundException;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.DriverMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.nio.file.AccessDeniedException;
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

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException("Driver with email: " + email + " not found"));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not found"));
    }
    private void validateAccess(Order order, String email) {
        if (order.getDriver() == null || !order.getDriver().getEmail().equals(email)) {
            try {
                throw new AccessDeniedException("You are not allowed to access this order");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

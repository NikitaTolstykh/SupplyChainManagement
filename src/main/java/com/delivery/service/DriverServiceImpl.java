package com.delivery.service;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.event.OrderStatusChangedEvent;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.exception.UserWithEmailNotFoundException;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.DriverMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DriverMapper driverMapper;
    private final DispatcherMapper dispatcherMapper;
    private final OrderStatusHistoryService orderStatusHistoryService;
    private final ApplicationEventPublisher eventPublisher;

    public DriverServiceImpl(OrderRepository orderRepository, UserRepository userRepository
            , DriverMapper driverMapper, DispatcherMapper dispatcherMapper
            , OrderStatusHistoryService orderStatusHistoryService, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.driverMapper = driverMapper;
        this.dispatcherMapper = dispatcherMapper;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<DriverOrderListItemDto> getAssignedOrders(String driverEmail) {
        User driver = findUserByEmail(driverEmail);
        List<Order> orders = orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.ASSIGNED);
        return driverMapper.toListDriverOrders(orders);
    }

    @Override
    public DispatcherOrderDetailsDto getOrderDetails(Long orderId, String driverEmail) {
        Order order = findOrderById(orderId);
        validateAccess(order, driverEmail);
        return dispatcherMapper.toDispatcherOrderDetailsDto(order);
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, String driverEmail) {
        Order order = findOrderById(orderId);
        validateAccess(order, driverEmail);
        User driver = findUserByEmail(driverEmail);

        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new IllegalStateException("Order is not assigned");
        }
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.IN_PROGRESS);

        orderStatusHistoryService.logStatusChange(order, oldStatus, OrderStatus.IN_PROGRESS, driver);

        Order savedOrder = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderStatusChangedEvent(savedOrder, oldStatus, OrderStatus.IN_PROGRESS));
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId, String driverEmail) {
        Order order = findOrderById(orderId);
        validateAccess(order, driverEmail);
        User driver = findUserByEmail(driverEmail);

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("You are not allowed to access this order");
        }

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.DELIVERED);

        orderStatusHistoryService.logStatusChange(order, oldStatus, OrderStatus.DELIVERED, driver);

        Order savedOrder = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(savedOrder, oldStatus, OrderStatus.DELIVERED));
    }

    @Override
    public List<DriverOrderListItemDto> getCompletedOrders(String driverEmail) {
        User driver = findUserByEmail(driverEmail);
        List<Order> orders = orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.DELIVERED);
        return driverMapper.toListDriverOrders(orders);
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

package com.delivery.service.impl;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.event.OrderStatusChangedEvent;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.DriverMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.service.interfaces.DriverService;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.util.OrderStatus;
import com.delivery.util.lookup.OrderLookupService;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.validation.AccessValidationService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    private final OrderRepository orderRepository;
    private final DriverMapper driverMapper;
    private final DispatcherMapper dispatcherMapper;
    private final OrderStatusHistoryService orderStatusHistoryService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserLookupService userLookupService;
    private final OrderLookupService orderLookupService;
    private final AccessValidationService accessValidationService;

    public DriverServiceImpl(OrderRepository orderRepository, DriverMapper driverMapper,
                             DispatcherMapper dispatcherMapper, OrderStatusHistoryService orderStatusHistoryService,
                             ApplicationEventPublisher eventPublisher, UserLookupService userLookupService,
                             OrderLookupService orderLookupService, AccessValidationService accessValidationService) {
        this.orderRepository = orderRepository;
        this.driverMapper = driverMapper;
        this.dispatcherMapper = dispatcherMapper;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.eventPublisher = eventPublisher;
        this.userLookupService = userLookupService;
        this.orderLookupService = orderLookupService;
        this.accessValidationService = accessValidationService;
    }

    @Override
    public List<DriverOrderListItemDto> getAssignedOrders(String driverEmail) {
        User driver = userLookupService.findUserByEmail(driverEmail);
        List<Order> orders = orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.ASSIGNED);
        return driverMapper.toListDriverOrders(orders);
    }

    @Override
    public DispatcherOrderDetailsDto getOrderDetails(Long orderId, String driverEmail) {
        Order order = orderLookupService.findOrderById(orderId);
        accessValidationService.validateDriverAccess(order, driverEmail);
        return dispatcherMapper.toDispatcherOrderDetailsDto(order);
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, String driverEmail) {
        Order order = orderLookupService.findOrderById(orderId);
        accessValidationService.validateDriverAccess(order, driverEmail);
        User driver = userLookupService.findUserByEmail(driverEmail);

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
        Order order = orderLookupService.findOrderById(orderId);
        accessValidationService.validateDriverAccess(order, driverEmail);
        User driver = userLookupService.findUserByEmail(driverEmail);

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
        User driver = userLookupService.findUserByEmail(driverEmail);
        List<Order> orders = orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.DELIVERED);
        return driverMapper.toListDriverOrders(orders);
    }
}

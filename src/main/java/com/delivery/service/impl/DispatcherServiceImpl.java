package com.delivery.service.impl;

import com.delivery.dto.*;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.event.OrderAssignedToDriverEvent;
import com.delivery.event.OrderStatusChangedEvent;
import com.delivery.exception.DriverNotFoundException;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.OrderMapper;
import com.delivery.mapper.OrderStatusHistoryMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.service.interfaces.DispatcherService;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.service.interfaces.PriceCalculatorService;
import com.delivery.util.OrderStatus;
import com.delivery.util.changeData.OrderDataService;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.validation.RoleValidator;
import com.delivery.util.lookup.OrderLookupService;
import com.delivery.util.security.CurrentUserService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DispatcherServiceImpl implements DispatcherService {
    private final DispatcherMapper dispatcherMapper;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RoleValidator roleValidator;
    private final PriceCalculatorService priceCalculatorService;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;
    private final OrderStatusHistoryService orderStatusHistoryService;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderLookupService orderLookupService;
    private final CurrentUserService currentUserService;
    private final UserLookupService userLookupService;
    private final OrderDataService orderDataService;

    public DispatcherServiceImpl(DispatcherMapper dispatcherMapper, OrderMapper orderMapper
            , UserRepository userRepository, OrderRepository orderRepository
            , RoleValidator roleValidator, PriceCalculatorService priceCalculatorService
            , OrderStatusHistoryMapper orderStatusHistoryMapper
            , OrderStatusHistoryService orderStatusHistoryService
            , ApplicationEventPublisher eventPublisher, OrderLookupService orderLookupService
            , CurrentUserService currentUserService, UserLookupService userLookupService
            , OrderDataService orderDataService) {
        this.dispatcherMapper = dispatcherMapper;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.roleValidator = roleValidator;
        this.priceCalculatorService = priceCalculatorService;
        this.orderStatusHistoryMapper = orderStatusHistoryMapper;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.eventPublisher = eventPublisher;
        this.orderLookupService = orderLookupService;
        this.currentUserService = currentUserService;
        this.userLookupService = userLookupService;
        this.orderDataService = orderDataService;
    }

    @Override
    public List<OrderListItemDto> getAllOrders() {
        return orderMapper.toListItemDto(orderRepository.findAll());
    }

    @Override
    @Cacheable(value = "order-details", key = "#id")
    public DispatcherOrderDetailsDto getOrderDetails(Long id) {
        Order order = orderLookupService.findOrderById(id);

        return dispatcherMapper.toDispatcherOrderDetailsDto(order);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"order-details", "available-drivers"}, allEntries = true)
    public void assignDriver(Long id, AssignDriverRequestDto dto) {
        Order order = orderLookupService.findOrderById(id);
        User driver = userLookupService.findUserById(dto.getDriverId());
        roleValidator.validateDriverRole(driver.getRole());
        User dispatcher = currentUserService.getCurrentUser();

        OrderStatus oldStatus = order.getStatus();

        orderDataService.assignDriverToOrder(order, driver);

        orderStatusHistoryService.logStatusChange(order, oldStatus, order.getStatus(), dispatcher);

        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderAssignedToDriverEvent(order, driver));
        eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, order.getStatus()));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"order-details", "available-drivers"}, allEntries = true)
    public void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = orderLookupService.findOrderById(id);
        User dispatcher = currentUserService.getCurrentUser();

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = dto.getStatus();

        orderDataService.changeOrderStatus(order, newStatus);

        orderStatusHistoryService.logStatusChange(order, oldStatus, newStatus, dispatcher);

        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, newStatus));
    }

    @Override
    @Transactional
    @CacheEvict(value = "order-details", key = "#id")
    public void updateOrderInfo(Long id, OrderRequestDto dto) {
        Order order = orderLookupService.findOrderById(id);
        orderDataService.updateOrderFields(order, dto);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"order-details", "available-drivers"}, allEntries = true)
    public void cancelOrder(Long id) {
        Order order = orderLookupService.findOrderById(id);
        OrderStatus oldStatus = order.getStatus();

        orderDataService.cancelOrder(order);

        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, OrderStatus.CANCELLED));
    }

    @Override
    @Cacheable(value = "available-drivers")
    public List<AvailableDriverDto> availableDrivers() {
        List<OrderStatus> activeStatuses = List.of(OrderStatus.ASSIGNED, OrderStatus.IN_PROGRESS);
        List<User> availableDrivers = userRepository.findAvailableDrivers(activeStatuses);
        return dispatcherMapper.toAvailableDriversDto(availableDrivers);
    }

    @Override
    public List<OrderStatusHistoryDto> getOrderStatusHistory(Long id) {
        return orderStatusHistoryMapper.toDtoList(orderStatusHistoryService.getOrderHistory(id));
    }
}

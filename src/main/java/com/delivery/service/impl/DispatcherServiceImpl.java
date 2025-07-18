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

    public DispatcherServiceImpl(DispatcherMapper dispatcherMapper, OrderMapper orderMapper
            , UserRepository userRepository, OrderRepository orderRepository
            , RoleValidator roleValidator, PriceCalculatorService priceCalculatorService
            , OrderStatusHistoryMapper orderStatusHistoryMapper
            , OrderStatusHistoryService orderStatusHistoryService
            , ApplicationEventPublisher eventPublisher, OrderLookupService orderLookupService
            , CurrentUserService currentUserService) {
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
        User driver = findAndValidateDriver(dto.getDriverId());
        User dispatcher = currentUserService.getCurrentUser();

        OrderStatus oldStatus = order.getStatus();

        order.setDriver(driver);
        order.setVehicle(driver.getVehicle());
        changeStatus(order);

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

        order.setStatus(newStatus);

        orderStatusHistoryService.logStatusChange(order, oldStatus, newStatus, dispatcher);

        orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, newStatus));
    }

    @Override
    @Transactional
    @CacheEvict(value = "order-details", key = "#id")
    public void updateOrderInfo(Long id, OrderRequestDto dto) {
        Order order = orderLookupService.findOrderById(id);
        updateOrderFields(order, dto);
        orderRepository.save(order);


    }

    @Override
    @Transactional
    @CacheEvict(value = {"order-details", "available-drivers"}, allEntries = true)
    public void cancelOrder(Long id) {
        if (!orderLookupService.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        Order order = orderLookupService.findOrderById(id);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);

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

    private void changeStatus(Order order) {
        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.ASSIGNED);
        }
    }

    private User findAndValidateDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver with id " + driverId + " not found"));
        roleValidator.validateDriverRole(driver.getRole());
        return driver;
    }

    private void updateOrderFields(Order order, OrderRequestDto dto) {
        order.setFromAddress(dto.getFromAddress());
        order.setToAddress(dto.getToAddress());
        order.setCargoType(dto.getCargoType());
        order.setCargoDescription(dto.getCargoDescription());
        order.setWeightKg(dto.getWeightKg());
        order.setComment(dto.getComment());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPickupTime(dto.getPickupTime());

        BigDecimal newPrice = priceCalculatorService.calculatePrice(dto.getWeightKg(), dto.getDistanceCategory());
        order.setPrice(newPrice);
    }
}

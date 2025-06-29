package com.delivery.service;

import com.delivery.dto.*;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.exception.DriverNotFoundException;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.OrderMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.OrderStatus;
import com.delivery.util.RoleValidator;
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

    public DispatcherServiceImpl(DispatcherMapper dispatcherMapper, OrderMapper orderMapper,
                                 UserRepository userRepository, OrderRepository orderRepository,
                                 RoleValidator roleValidator, PriceCalculatorService priceCalculatorService) {
        this.dispatcherMapper = dispatcherMapper;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.roleValidator = roleValidator;
        this.priceCalculatorService = priceCalculatorService;
    }

    @Override
    public List<OrderListItemDto> getAllOrders() {
        return orderMapper.toListItemDto(orderRepository.findAll());
    }

    @Override
    public DispatcherOrderDetailsDto getOrderDetails(Long id) {
        Order order = findOrderById(id);

        return dispatcherMapper.toDispatcherOrderDetailsDto(order);
    }

    @Override
    public void assignDriver(Long id, AssignDriverRequestDto dto) {
        Order order = findOrderById(id);
        User driver = findAndValidateDriver(dto.getDriverId());


        order.setDriver(driver);

        changeStatus(order);

        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = findOrderById(id);

        OrderStatus newStatus = dto.getStatus();
        order.setStatus(newStatus);

        orderRepository.save(order);
    }

    @Override
    public void updateOrderInfo(Long id, OrderRequestDto dto) {
        Order order = findOrderById(id);
        updateOrderFields(order, dto);
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<AvailableDriverDto> availableDrivers() {
        List<OrderStatus> activeStatuses = List.of(OrderStatus.ASSIGNED, OrderStatus.IN_PROGRESS);
        List<User> availableDrivers = userRepository.findAvailableDrivers(activeStatuses);
        return dispatcherMapper.toAvailableDriversDto(availableDrivers);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + "not found"));
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

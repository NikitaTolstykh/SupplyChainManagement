package com.delivery.service;

import com.delivery.dto.*;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.OrderMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.OrderStatus;
import com.delivery.util.RoleValidator;
import org.springframework.stereotype.Service;

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
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return dispatcherMapper.toDispatcherOrderDetailsDto(order);
    }

    @Override
    public void assignDriver(Long id, AssignDriverRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User driver = userRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + dto.getDriverId()));

        roleValidator.validateDriverRole(driver.getRole());

        order.setDriver(driver);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long id, UpdateOrderStatusRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus newStatus = dto.getStatus();
        order.setStatus(newStatus);

        orderRepository.save(order);
    }

    @Override
    public void updateOrderInfo(Long id, OrderRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setFromAddress(dto.getFromAddress());
        order.setToAddress(dto.getToAddress());
        order.setCargoType(dto.getCargoType());
        order.setCargoDescription(dto.getCargoDescription());
        order.setWeightKg(dto.getWeightKg());
        order.setComment(dto.getComment());
        order.setPrice(priceCalculatorService.calculatePrice(dto.getWeightKg(), dto.getDistanceCategory()));
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPickupTime(dto.getPickupTime());

        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<AvailableDriverDto> availableDrivers() {
        List<OrderStatus> activeStatuses = List.of(OrderStatus.ASSIGNED, OrderStatus.IN_PROGRESS);
        List<User> availableDrivers = userRepository.findAvailableDrivers(activeStatuses);
        return dispatcherMapper.toAvailableDriversDto(availableDrivers);
    }
}

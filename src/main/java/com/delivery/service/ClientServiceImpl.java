package com.delivery.service;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.dto.OrderStatusHistoryDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.event.OrderCreatedEvent;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.exception.UserWithEmailNotFoundException;
import com.delivery.mapper.OrderMapper;
import com.delivery.mapper.OrderStatusHistoryMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.OrderLookupService;
import com.delivery.util.UserLookupService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final PriceCalculatorService priceCalculatorService;
    private final OrderStatusHistoryService orderStatusHistoryService;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserLookupService userLookupService;
    private final OrderLookupService orderLookupService;

    public ClientServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                             OrderMapper orderMapper, PriceCalculatorService priceCalculatorService,
                             OrderStatusHistoryService orderStatusHistoryService, OrderStatusHistoryMapper orderStatusHistoryMapper,
                             ApplicationEventPublisher eventPublisher, UserLookupService userLookupService, OrderLookupService orderLookupService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.priceCalculatorService = priceCalculatorService;
        this.orderStatusHistoryService = orderStatusHistoryService;
        this.orderStatusHistoryMapper = orderStatusHistoryMapper;
        this.eventPublisher = eventPublisher;
        this.userLookupService = userLookupService;
        this.orderLookupService = orderLookupService;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"client-orders", "client-statistics"}, key = "#email")
    public OrderDetailsDto createOrder(OrderRequestDto dto, String email) {
        User client = userLookupService.findUserByEmail(email);

        Order order = orderMapper.toEntity(dto);
        order.setClient(client);
        order.setPrice(priceCalculatorService.calculatePrice(dto.getWeightKg(), dto.getDistanceCategory()));

        Order savedOrder = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder));

        return orderMapper.toDetailsDto(savedOrder);
    }

    @Override
    @Cacheable(value = "client-orders", key = "#email")
    public List<OrderListItemDto> getClientOrders(String email) {
        List<Order> order = orderRepository.findAllByClient_Email(email);
        return orderMapper.toListItemDto(order);
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long orderId, String email) {
        Order order = orderLookupService.findOrderById(orderId);
        emailValidation(order, email);

        return orderMapper.toDetailsDto(order);
    }

    @Override
    public List<OrderListItemDto> getOrdersAvailableForRating(String email) {
        List<Order> orders = orderRepository.findOrdersEligibleForRatingByClientEmail(email);
        return orderMapper.toListItemDto(orders);
    }

    @Override
    public List<OrderStatusHistoryDto> getOrderStatusHistory(Long orderId, String email) {
        Order order = orderLookupService.findOrderById(orderId);
        emailValidation(order, email);

        return orderStatusHistoryMapper.toDtoList(orderStatusHistoryService.getOrderHistory(orderId));
    }

    private void emailValidation(Order order, String email) {
        if (!order.getClient().getEmail().equals(email)) {
            throw new IllegalArgumentException("Access denied to this order");
        }

    }


}


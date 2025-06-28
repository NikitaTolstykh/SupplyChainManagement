package com.delivery.service;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.exception.UserWithEmailNotFoundException;
import com.delivery.mapper.OrderMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final PriceCalculatorService priceCalculatorService;

    public ClientServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                             OrderMapper orderMapper, PriceCalculatorService priceCalculatorService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.priceCalculatorService = priceCalculatorService;
    }

    @Override
    public OrderDetailsDto createOrder(OrderRequestDto dto, String email) {
        User client = findUserByEmail(email);

        Order order = orderMapper.toEntity(dto);
        order.setClient(client);

        order.setPrice(priceCalculatorService.calculatePrice(dto.getWeightKg(), dto.getDistanceCategory()));

        orderRepository.save(order);
        return orderMapper.toDetailsDto(order);
    }

    @Override
    public List<OrderListItemDto> getClientOrders(String email) {
        List<Order> order = orderRepository.findAllByClient_Email(email);
        return orderMapper.toListItemDto(order);
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long orderId, String email) {
        Order order = findOrderById(orderId);
        emailValidation(order, email);

        return orderMapper.toDetailsDto(order);
    }


    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException("User with email: " + email + " not found"));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not found"));
    }

    private void emailValidation(Order order, String email) {
        if (!order.getClient().getEmail().equals(email)) {
            try {
                throw new AccessDeniedException("You are not allowed to access this order");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


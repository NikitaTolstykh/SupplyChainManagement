package com.delivery.service;

import com.delivery.dto.OrderRatingRequestDto;
import com.delivery.dto.OrderRatingResponseDto;
import com.delivery.entity.Order;
import com.delivery.entity.OrderRating;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.exception.RatingAlreadyExistsException;
import com.delivery.mapper.OrderRatingMapper;
import com.delivery.repository.OrderRatingRepository;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.AccessValidationService;
import com.delivery.util.OrderLookupService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class OrderServiceRatingImpl implements OrderRatingService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderRatingRepository orderRatingRepository;
    private final OrderRatingMapper orderRatingMapper;
    private final OrderLookupService orderLookupService;
    private final AccessValidationService accessValidationService;

    public OrderServiceRatingImpl(OrderRepository orderRepository, UserRepository userRepository
            , OrderRatingRepository orderRatingRepository, OrderRatingMapper orderRatingMapper
            , OrderLookupService orderLookupService, AccessValidationService accessValidationService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderRatingRepository = orderRatingRepository;
        this.orderRatingMapper = orderRatingMapper;
        this.orderLookupService = orderLookupService;
        this.accessValidationService = accessValidationService;
    }

    @Override
    @Transactional
    public OrderRatingResponseDto rateOrder(Long orderId, OrderRatingRequestDto dto, String clientEmail) {
        Order order = orderLookupService.findOrderById(orderId);
        accessValidationService.validateOrderAccess(order, clientEmail);
        OrderRating rating = fillRatingFields(order, dto);

        orderRatingRepository.save(rating);

        return orderRatingMapper.toDto(rating);
    }

    @Override
    public List<OrderRatingResponseDto> opinionList() {
        return orderRatingMapper.opinionList(orderRatingRepository.findAll());
    }

    private OrderRating fillRatingFields(Order order, OrderRatingRequestDto dto) {
        OrderRating rating = new OrderRating();
        rating.setOrder(order);
        rating.setClient(order.getClient());
        rating.setStars(dto.getStars());
        rating.setComment(dto.getComment());
        return rating;
    }
}


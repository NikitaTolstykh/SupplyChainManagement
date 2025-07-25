package com.delivery.service;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.dto.OrderStatusHistoryDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.event.OrderCreatedEvent;
import com.delivery.mapper.OrderMapper;
import com.delivery.mapper.OrderStatusHistoryMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.service.impl.ClientServiceImpl;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.service.interfaces.PriceCalculatorService;
import com.delivery.util.DistanceCategory;
import com.delivery.util.OrderStatus;
import com.delivery.util.PaymentMethod;
import com.delivery.util.Role;
import com.delivery.util.lookup.OrderLookupService;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.validation.AccessValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PriceCalculatorService priceCalculatorService;

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService;

    @Mock
    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UserLookupService userLookupService;

    @Mock
    private OrderLookupService orderLookupService;

    @Mock
    private AccessValidationService accessValidationService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private User client;
    private Order order;
    private OrderRequestDto orderRequestDto;
    private OrderDetailsDto orderDetailsDto;
    private OrderListItemDto orderListItemDto;
    private OrderStatusHistoryDto orderStatusHistoryDto;
    private String clientEmail;

    @BeforeEach
    void setUp() {
        clientEmail = "client@example.com";

        client = new User();
        client.setId(1L);
        client.setEmail(clientEmail);
        client.setRole(Role.CLIENT);
        client.setFirstName("John");
        client.setLastName("Doe");

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setFromAddress("From Address");
        order.setToAddress("To Address");
        order.setCargoType("Electronics");
        order.setWeightKg(new BigDecimal("5.5"));
        order.setDistanceCategory(DistanceCategory.MEDIUM);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setPrice(new BigDecimal("100.00"));
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setFromAddress("From Address");
        orderRequestDto.setToAddress("To Address");
        orderRequestDto.setCargoType("Electronics");
        orderRequestDto.setWeightKg(new BigDecimal("5.5"));
        orderRequestDto.setDistanceCategory(DistanceCategory.MEDIUM);
        orderRequestDto.setPaymentMethod(PaymentMethod.CASH);
        orderRequestDto.setPickupTime(LocalDateTime.now().plusHours(2));
        orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setId(1L);
        orderDetailsDto.setFromAddress("From Address");
        orderDetailsDto.setToAddress("To Address");
        orderDetailsDto.setCargoType("Electronics");
        orderDetailsDto.setWeightKg(new BigDecimal("5.5"));
        orderDetailsDto.setPrice(new BigDecimal("100.00"));
        orderDetailsDto.setStatus("CREATED");

        orderListItemDto = new OrderListItemDto();
        orderListItemDto.setId(1L);
        orderListItemDto.setFromAddress("From Address");
        orderListItemDto.setToAddress("To Address");
        orderListItemDto.setStatus("CREATED");
        orderListItemDto.setPrice(new BigDecimal("100.00"));

        orderStatusHistoryDto = new OrderStatusHistoryDto();
        orderStatusHistoryDto.setId(1L);
        orderStatusHistoryDto.setToStatus(OrderStatus.CREATED);
        orderStatusHistoryDto.setChangedAt(LocalDateTime.now());
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        BigDecimal calculatedPrice = new BigDecimal("100.00");

        when(userLookupService.findUserByEmail(clientEmail)).thenReturn(client);
        when(orderMapper.toEntity(orderRequestDto)).thenReturn(order);
        when(priceCalculatorService.calculatePrice(orderRequestDto.getWeightKg(), orderRequestDto.getDistanceCategory()))
                .thenReturn(calculatedPrice);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDetailsDto(order)).thenReturn(orderDetailsDto);

        OrderDetailsDto result = clientService.createOrder(orderRequestDto, clientEmail);

        assertNotNull(result);
        assertEquals(orderDetailsDto.getId(), result.getId());
        assertEquals(orderDetailsDto.getFromAddress(), result.getFromAddress());
        assertEquals(orderDetailsDto.getPrice(), result.getPrice());

        verify(userLookupService).findUserByEmail(clientEmail);
        verify(orderMapper).toEntity(orderRequestDto);
        verify(priceCalculatorService).calculatePrice(orderRequestDto.getWeightKg(), orderRequestDto.getDistanceCategory());
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toDetailsDto(order);
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void getClientOrders_ShouldReturnOrderList() {
        List<Order> orders = List.of(order);
        List<OrderListItemDto> orderListItemDtos = List.of(orderListItemDto);

        when(orderRepository.findAllByClient_Email(clientEmail)).thenReturn(orders);
        when(orderMapper.toListItemDto(orders)).thenReturn(orderListItemDtos);

        List<OrderListItemDto> result = clientService.getClientOrders(clientEmail);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderListItemDto.getId(), result.get(0).getId());
        assertEquals(orderListItemDto.getFromAddress(), result.get(0).getFromAddress());

        verify(orderRepository).findAllByClient_Email(clientEmail);
        verify(orderMapper).toListItemDto(orders);
    }

    @Test
    void getOrderDetails_ShouldReturnOrderDetails_WhenOrderExistsAndEmailMatches() {
        Long orderId = 1L;

        when(orderLookupService.findOrderById(orderId)).thenReturn(order);
        doNothing().when(accessValidationService).validateOrderAccess(order, clientEmail);
        when(orderMapper.toDetailsDto(order)).thenReturn(orderDetailsDto);

        OrderDetailsDto result = clientService.getOrderDetails(orderId, clientEmail);

        assertNotNull(result);
        assertEquals(orderDetailsDto.getId(), result.getId());
        assertEquals(orderDetailsDto.getFromAddress(), result.getFromAddress());

        verify(orderLookupService).findOrderById(orderId);
        verify(accessValidationService).validateOrderAccess(order, clientEmail);
        verify(orderMapper).toDetailsDto(order);
    }
    @Test
    void getOrdersAvailableForRating_ShouldReturnEligibleOrders() {
        List<Order> orders = List.of(order);
        List<OrderListItemDto> orderListItemDtos = List.of(orderListItemDto);

        when(orderRepository.findOrdersEligibleForRatingByClientEmail(clientEmail)).thenReturn(orders);
        when(orderMapper.toListItemDto(orders)).thenReturn(orderListItemDtos);

        List<OrderListItemDto> result = clientService.getOrdersAvailableForRating(clientEmail);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderListItemDto.getId(), result.get(0).getId());

        verify(orderRepository).findOrdersEligibleForRatingByClientEmail(clientEmail);
        verify(orderMapper).toListItemDto(orders);
    }

    @Test
    void getOrderStatusHistory_ShouldReturnStatusHistory_WhenOrderExistsAndEmailMatches() {
        Long orderId = 1L;
        List<OrderStatusHistoryDto> historyDtos = List.of(orderStatusHistoryDto);

        when(orderLookupService.findOrderById(orderId)).thenReturn(order);
        doNothing().when(accessValidationService).validateOrderAccess(order, clientEmail);
        when(orderStatusHistoryService.getOrderHistory(orderId)).thenReturn(List.of());
        when(orderStatusHistoryMapper.toDtoList(any())).thenReturn(historyDtos);

        List<OrderStatusHistoryDto> result = clientService.getOrderStatusHistory(orderId, clientEmail);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderStatusHistoryDto.getId(), result.get(0).getId());

        verify(orderLookupService).findOrderById(orderId);
        verify(accessValidationService).validateOrderAccess(order, clientEmail);
        verify(orderStatusHistoryService).getOrderHistory(orderId);
        verify(orderStatusHistoryMapper).toDtoList(any());
    }
}
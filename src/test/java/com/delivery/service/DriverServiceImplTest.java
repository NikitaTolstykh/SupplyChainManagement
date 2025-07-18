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
import com.delivery.service.impl.DriverServiceImpl;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.util.OrderStatus;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DispatcherMapper dispatcherMapper;

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DriverServiceImpl driverService;

    private User driver;
    private Order order;
    private DriverOrderListItemDto driverDto;
    private DispatcherOrderDetailsDto detailsDto;

    @BeforeEach
    void setUp() {
        driver = new User();
        driver.setId(1L);
        driver.setEmail("driver@example.com");

        order = new Order();
        order.setId(10L);
        order.setFromAddress("From");
        order.setToAddress("To");
        order.setStatus(OrderStatus.ASSIGNED);
        order.setPickupTime(LocalDateTime.now());
        order.setPrice(BigDecimal.TEN);
        order.setDriver(driver);

        driverDto = new DriverOrderListItemDto(10L, "From", "To", OrderStatus.ASSIGNED, order.getPickupTime(), BigDecimal.TEN);

        detailsDto = new DispatcherOrderDetailsDto();
        detailsDto.setId(order.getId());
        detailsDto.setFromAddress(order.getFromAddress());
        detailsDto.setToAddress(order.getToAddress());
    }

    @Test
    void getAssignedOrders_ShouldReturnMappedOrders() {
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));
        when(orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.ASSIGNED)).thenReturn(List.of(order));
        when(driverMapper.toListDriverOrders(List.of(order))).thenReturn(List.of(driverDto));

        List<DriverOrderListItemDto> result = driverService.getAssignedOrders("driver@example.com");

        assertEquals(1, result.size());
        assertEquals(driverDto.getId(), result.get(0).getId());
    }

    @Test
    void getCompletedOrders_ShouldReturnMappedOrders() {
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));
        when(orderRepository.findAllByDriver_IdAndStatus(driver.getId(), OrderStatus.DELIVERED)).thenReturn(List.of(order));
        when(driverMapper.toListDriverOrders(List.of(order))).thenReturn(List.of(driverDto));

        List<DriverOrderListItemDto> result = driverService.getCompletedOrders("driver@example.com");

        assertEquals(1, result.size());
        assertEquals(driverDto.getId(), result.get(0).getId());
    }

    @Test
    void getOrderDetails_ShouldReturnOrderDetailsDto() {
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(dispatcherMapper.toDispatcherOrderDetailsDto(order)).thenReturn(detailsDto);

        DispatcherOrderDetailsDto result = driverService.getOrderDetails(10L, "driver@example.com");

        assertEquals(detailsDto.getId(), result.getId());
    }

    @Test
    void acceptOrder_ShouldUpdateStatusToInProgress() {
        order.setStatus(OrderStatus.ASSIGNED);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));
        when(orderRepository.save(order)).thenReturn(order);

        driverService.acceptOrder(10L, "driver@example.com");

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        verify(orderStatusHistoryService).logStatusChange(order, OrderStatus.ASSIGNED, OrderStatus.IN_PROGRESS, driver);
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void completeOrder_ShouldUpdateStatusToDelivered() {
        order.setStatus(OrderStatus.IN_PROGRESS);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));
        when(orderRepository.save(order)).thenReturn(order);

        driverService.completeOrder(10L, "driver@example.com");

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderStatusHistoryService).logStatusChange(order, OrderStatus.IN_PROGRESS, OrderStatus.DELIVERED, driver);
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void getAssignedOrders_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserWithEmailNotFoundException.class,
                () -> driverService.getAssignedOrders("notfound@example.com"));
    }

    @Test
    void getOrderDetails_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> driverService.getOrderDetails(999L, "driver@example.com"));
    }

    @Test
    void acceptOrder_ShouldThrowException_WhenOrderNotAssigned() {
        order.setStatus(OrderStatus.IN_PROGRESS);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));

        assertThrows(IllegalStateException.class,
                () -> driverService.acceptOrder(10L, "driver@example.com"));
    }

    @Test
    void completeOrder_ShouldThrowException_WhenOrderNotInProgress() {
        order.setStatus(OrderStatus.ASSIGNED);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(userRepository.findUserByEmail("driver@example.com")).thenReturn(Optional.of(driver));

        assertThrows(IllegalArgumentException.class,
                () -> driverService.completeOrder(10L, "driver@example.com"));
    }
}

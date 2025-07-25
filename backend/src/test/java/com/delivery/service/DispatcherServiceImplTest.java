package com.delivery.service;

import com.delivery.dto.*;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.event.OrderAssignedToDriverEvent;
import com.delivery.event.OrderStatusChangedEvent;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.OrderMapper;
import com.delivery.mapper.OrderStatusHistoryMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.service.impl.DispatcherServiceImpl;
import com.delivery.service.interfaces.OrderStatusHistoryService;
import com.delivery.util.*;
import com.delivery.util.lookup.OrderLookupService;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.security.CurrentUserService;
import com.delivery.util.updateData.OrderDataService;
import com.delivery.util.validation.RoleValidator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DispatcherServiceImplTest {

    @Mock
    private DispatcherMapper dispatcherMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private OrderLookupService orderLookupService;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private UserLookupService userLookupService;

    @Mock
    private OrderDataService orderDataService;

    @InjectMocks
    private DispatcherServiceImpl dispatcherService;

    private Order order;
    private User client;
    private User driver;
    private User dispatcher;
    private Vehicle vehicle;
    private OrderListItemDto orderListItemDto;
    private DispatcherOrderDetailsDto dispatcherOrderDetailsDto;
    private AssignDriverRequestDto assignDriverRequestDto;
    private UpdateOrderStatusRequestDto updateOrderStatusRequestDto;
    private OrderRequestDto orderRequestDto;
    private AvailableDriverDto availableDriverDto;
    private OrderStatusHistoryDto orderStatusHistoryDto;

    @BeforeEach
    void setUp() {
        client = new User();
        client.setId(1L);
        client.setEmail("client@example.com");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setPhone("1234567890");
        client.setRole(Role.CLIENT);

        driver = new User();
        driver.setId(2L);
        driver.setEmail("driver@example.com");
        driver.setFirstName("Ivan");
        driver.setLastName("Petrov");
        driver.setPhone("0987654321");
        driver.setRole(Role.DRIVER);

        dispatcher = new User();
        dispatcher.setId(3L);
        dispatcher.setEmail("dispatcher@example.com");
        dispatcher.setFirstName("Anna");
        dispatcher.setLastName("Sidorova");
        dispatcher.setRole(Role.DISPATCHER);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setLicensePlate("ABC123");
        vehicle.setDriver(driver);

        driver.setVehicle(vehicle);
        order = new Order();
        order.setId(1L);
        order.setFromAddress("From Address");
        order.setToAddress("To Address");
        order.setCargoType("Electronics");
        order.setCargoDescription("Laptop");
        order.setWeightKg(new BigDecimal("5.5"));
        order.setComment("Handle with care");
        order.setPrice(new BigDecimal("100.00"));
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setStatus(OrderStatus.CREATED);
        order.setPickupTime(LocalDateTime.now().plusHours(2));
        order.setDistanceCategory(DistanceCategory.MEDIUM);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setClient(client);

        orderListItemDto = new OrderListItemDto();
        orderListItemDto.setId(1L);
        orderListItemDto.setFromAddress("From Address");
        orderListItemDto.setToAddress("To Address");
        orderListItemDto.setStatus("CREATED");
        orderListItemDto.setPrice(new BigDecimal("100.00"));
        orderListItemDto.setCreatedAt(LocalDateTime.now());

        dispatcherOrderDetailsDto = new DispatcherOrderDetailsDto();
        dispatcherOrderDetailsDto.setId(1L);
        dispatcherOrderDetailsDto.setFromAddress("From Address");
        dispatcherOrderDetailsDto.setToAddress("To Address");
        dispatcherOrderDetailsDto.setCargoType("Electronics");
        dispatcherOrderDetailsDto.setCargoDescription("Laptop");
        dispatcherOrderDetailsDto.setWeightKg(new BigDecimal("5.5"));
        dispatcherOrderDetailsDto.setComment("Handle with care");
        dispatcherOrderDetailsDto.setPrice(new BigDecimal("100.00"));
        dispatcherOrderDetailsDto.setPaymentMethod(PaymentMethod.CASH);
        dispatcherOrderDetailsDto.setStatus(OrderStatus.CREATED);
        dispatcherOrderDetailsDto.setPickupTime(LocalDateTime.now().plusHours(2));
        dispatcherOrderDetailsDto.setCreatedAt(LocalDateTime.now());
        dispatcherOrderDetailsDto.setUpdatedAt(LocalDateTime.now());
        dispatcherOrderDetailsDto.setClientId(1L);
        dispatcherOrderDetailsDto.setClientName("John Doe");
        dispatcherOrderDetailsDto.setClientEmail("client@example.com");
        dispatcherOrderDetailsDto.setClientPhoneNumber("1234567890");

        assignDriverRequestDto = new AssignDriverRequestDto();
        assignDriverRequestDto.setDriverId(2L);

        updateOrderStatusRequestDto = new UpdateOrderStatusRequestDto();
        updateOrderStatusRequestDto.setStatus(OrderStatus.IN_PROGRESS);

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setFromAddress("New From Address");
        orderRequestDto.setToAddress("New To Address");
        orderRequestDto.setCargoType("Books");
        orderRequestDto.setCargoDescription("Academic books");
        orderRequestDto.setWeightKg(new BigDecimal("3.0"));
        orderRequestDto.setComment("New comment");
        orderRequestDto.setPaymentMethod(PaymentMethod.CARD);
        orderRequestDto.setPickupTime(LocalDateTime.now().plusHours(3));
        orderRequestDto.setDistanceCategory(DistanceCategory.SHORT);

        availableDriverDto = new AvailableDriverDto();
        availableDriverDto.setId(2L);
        availableDriverDto.setFirstName("Ivan");
        availableDriverDto.setLastName("Petrov");
        availableDriverDto.setPhone("0987654321");
        availableDriverDto.setEmail("driver@example.com");
        availableDriverDto.setVehicleBrand("Toyota");
        availableDriverDto.setVehicleModel("Camry");
        availableDriverDto.setLicensePlate("ABC123");

        orderStatusHistoryDto = new OrderStatusHistoryDto();
        orderStatusHistoryDto.setId(1L);
        orderStatusHistoryDto.setToStatus(OrderStatus.CREATED);
        orderStatusHistoryDto.setChangedAt(LocalDateTime.now());
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        List<Order> orders = List.of(order);
        List<OrderListItemDto> orderListItemDtos = List.of(orderListItemDto);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toListItemDto(orders)).thenReturn(orderListItemDtos);

        List<OrderListItemDto> result = dispatcherService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderListItemDto.getId(), result.get(0).getId());

        verify(orderRepository).findAll();
        verify(orderMapper).toListItemDto(orders);
    }

    @Test
    void getOrderDetails_ShouldReturnOrderDetails_WhenOrderExists() {
        when(orderLookupService.findOrderById(1L)).thenReturn(order);
        when(dispatcherMapper.toDispatcherOrderDetailsDto(order)).thenReturn(dispatcherOrderDetailsDto);

        DispatcherOrderDetailsDto result = dispatcherService.getOrderDetails(1L);

        assertNotNull(result);
        assertEquals(dispatcherOrderDetailsDto.getId(), result.getId());
        assertEquals(dispatcherOrderDetailsDto.getFromAddress(), result.getFromAddress());

        verify(orderLookupService).findOrderById(1L);
        verify(dispatcherMapper).toDispatcherOrderDetailsDto(order);
    }

    @Test
    void assignDriver_ShouldAssignDriverToOrder() {
        when(orderLookupService.findOrderById(1L)).thenReturn(order);
        when(userLookupService.findUserById(2L)).thenReturn(driver);
        doNothing().when(roleValidator).validateDriverRole(driver.getRole());
        when(currentUserService.getCurrentUser()).thenReturn(dispatcher);
        doNothing().when(orderDataService).assignDriverToOrder(order, driver);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.assignDriver(1L, assignDriverRequestDto);

        verify(orderLookupService).findOrderById(1L);
        verify(userLookupService).findUserById(2L);
        verify(roleValidator).validateDriverRole(driver.getRole());
        verify(currentUserService).getCurrentUser();
        verify(orderDataService).assignDriverToOrder(order, driver);
        verify(orderRepository).save(order);
        verify(orderStatusHistoryService).logStatusChange(eq(order), any(OrderStatus.class), any(OrderStatus.class), eq(dispatcher));
        verify(eventPublisher).publishEvent(any(OrderAssignedToDriverEvent.class));
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        when(orderLookupService.findOrderById(1L)).thenReturn(order);
        when(currentUserService.getCurrentUser()).thenReturn(dispatcher);
        doNothing().when(orderDataService).changeOrderStatus(order, OrderStatus.IN_PROGRESS);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.updateOrderStatus(1L, updateOrderStatusRequestDto);

        verify(orderLookupService).findOrderById(1L);
        verify(currentUserService).getCurrentUser();
        verify(orderDataService).changeOrderStatus(order, OrderStatus.IN_PROGRESS);
        verify(orderRepository).save(order);
        verify(orderStatusHistoryService).logStatusChange(eq(order), any(OrderStatus.class), eq(OrderStatus.IN_PROGRESS), eq(dispatcher));
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void updateOrderInfo_ShouldUpdateOrderFields() {
        when(orderLookupService.findOrderById(1L)).thenReturn(order);
        doNothing().when(orderDataService).updateOrderFields(order, orderRequestDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.updateOrderInfo(1L, orderRequestDto);

        verify(orderLookupService).findOrderById(1L);
        verify(orderDataService).updateOrderFields(order, orderRequestDto);
        verify(orderRepository).save(order);
    }

    @Test
    void cancelOrder_ShouldCancelOrder() {
        when(orderLookupService.findOrderById(1L)).thenReturn(order);
        doNothing().when(orderDataService).cancelOrder(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.cancelOrder(1L);
        verify(orderLookupService).findOrderById(1L);
        verify(orderDataService).cancelOrder(order);
        verify(orderRepository).save(order);
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void availableDrivers_ShouldReturnAvailableDrivers() {
        List<User> drivers = List.of(driver);
        List<AvailableDriverDto> availableDriverDtos = List.of(availableDriverDto);

        when(userRepository.findAvailableDrivers(any())).thenReturn(drivers);
        when(dispatcherMapper.toAvailableDriversDto(drivers)).thenReturn(availableDriverDtos);

        List<AvailableDriverDto> result = dispatcherService.availableDrivers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(availableDriverDto.getId(), result.get(0).getId());

        verify(userRepository).findAvailableDrivers(any());
        verify(dispatcherMapper).toAvailableDriversDto(drivers);
    }

    @Test
    void getOrderStatusHistory_ShouldReturnStatusHistory() {
        List<OrderStatusHistoryDto> historyDtos = List.of(orderStatusHistoryDto);

        when(orderStatusHistoryService.getOrderHistory(1L)).thenReturn(List.of());
        when(orderStatusHistoryMapper.toDtoList(any())).thenReturn(historyDtos);

        List<OrderStatusHistoryDto> result = dispatcherService.getOrderStatusHistory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderStatusHistoryDto.getId(), result.get(0).getId());

        verify(orderStatusHistoryService).getOrderHistory(1L);
        verify(orderStatusHistoryMapper).toDtoList(any());
    }
}
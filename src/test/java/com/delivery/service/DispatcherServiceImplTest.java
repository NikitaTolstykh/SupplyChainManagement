package com.delivery.service;

import com.delivery.dto.*;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.event.OrderAssignedToDriverEvent;
import com.delivery.event.OrderStatusChangedEvent;
import com.delivery.exception.DriverNotFoundException;
import com.delivery.exception.OrderNotFoundException;
import com.delivery.mapper.DispatcherMapper;
import com.delivery.mapper.OrderMapper;
import com.delivery.mapper.OrderStatusHistoryMapper;
import com.delivery.repository.OrderRepository;
import com.delivery.repository.UserRepository;
import com.delivery.util.*;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private PriceCalculatorService priceCalculatorService;

    @Mock
    private OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Mock
    private OrderStatusHistoryService orderStatusHistoryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

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
        // Создание пользователей
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

        // Создание транспортного средства
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setLicensePlate("ABC123");
        vehicle.setDriver(driver);

        driver.setVehicle(vehicle);

        // Создание заказа
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

        // Создание DTO
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
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(dispatcherMapper.toDispatcherOrderDetailsDto(order)).thenReturn(dispatcherOrderDetailsDto);

        DispatcherOrderDetailsDto result = dispatcherService.getOrderDetails(1L);

        assertNotNull(result);
        assertEquals(dispatcherOrderDetailsDto.getId(), result.getId());
        assertEquals(dispatcherOrderDetailsDto.getFromAddress(), result.getFromAddress());

        verify(orderRepository).findById(1L);
        verify(dispatcherMapper).toDispatcherOrderDetailsDto(order);
    }

    @Test
    void getOrderDetails_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> dispatcherService.getOrderDetails(1L));

        assertEquals("Order with id 1not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoInteractions(dispatcherMapper);
    }

    @Test
    void assignDriver_ShouldAssignDriverToOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.of(driver));
        doNothing().when(roleValidator).validateDriverRole(driver.getRole());
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.assignDriver(1L, assignDriverRequestDto);

        assertEquals(driver, order.getDriver());
        assertEquals(vehicle, order.getVehicle());
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());

        verify(orderRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(roleValidator).validateDriverRole(driver.getRole());
        verify(orderRepository).save(order);
        verify(orderStatusHistoryService).logStatusChange(eq(order), eq(OrderStatus.CREATED), eq(OrderStatus.ASSIGNED), any(User.class));
        verify(eventPublisher).publishEvent(any(OrderAssignedToDriverEvent.class));
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void assignDriver_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> dispatcherService.assignDriver(1L, assignDriverRequestDto));

        assertEquals("Order with id 1not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoInteractions(userRepository, roleValidator, orderStatusHistoryService, eventPublisher);
    }

    @Test
    void assignDriver_ShouldThrowException_WhenDriverNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class,
                () -> dispatcherService.assignDriver(1L, assignDriverRequestDto));

        assertEquals("Driver with id 2 not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verify(userRepository).findById(2L);
        verifyNoInteractions(roleValidator, orderStatusHistoryService, eventPublisher);
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.updateOrderStatus(1L, updateOrderStatusRequestDto);

        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
        verify(orderStatusHistoryService).logStatusChange(eq(order), eq(OrderStatus.CREATED), eq(OrderStatus.IN_PROGRESS), any(User.class));
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void updateOrderStatus_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> dispatcherService.updateOrderStatus(1L, updateOrderStatusRequestDto));

        assertEquals("Order with id 1not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoInteractions(orderStatusHistoryService, eventPublisher);
    }

    @Test
    void updateOrderInfo_ShouldUpdateOrderFields() {
        BigDecimal newPrice = new BigDecimal("150.00");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(priceCalculatorService.calculatePrice(orderRequestDto.getWeightKg(), orderRequestDto.getDistanceCategory()))
                .thenReturn(newPrice);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.updateOrderInfo(1L, orderRequestDto);

        assertEquals(orderRequestDto.getFromAddress(), order.getFromAddress());
        assertEquals(orderRequestDto.getToAddress(), order.getToAddress());
        assertEquals(orderRequestDto.getCargoType(), order.getCargoType());
        assertEquals(orderRequestDto.getCargoDescription(), order.getCargoDescription());
        assertEquals(orderRequestDto.getWeightKg(), order.getWeightKg());
        assertEquals(orderRequestDto.getComment(), order.getComment());
        assertEquals(orderRequestDto.getPaymentMethod(), order.getPaymentMethod());
        assertEquals(orderRequestDto.getPickupTime(), order.getPickupTime());
        assertEquals(newPrice, order.getPrice());

        verify(orderRepository).findById(1L);
        verify(priceCalculatorService).calculatePrice(orderRequestDto.getWeightKg(), orderRequestDto.getDistanceCategory());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderInfo_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> dispatcherService.updateOrderInfo(1L, orderRequestDto));

        assertEquals("Order with id 1not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verifyNoInteractions(priceCalculatorService);
    }

    @Test
    void cancelOrder_ShouldCancelOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        dispatcherService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepository).existsById(1L);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
        verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> dispatcherService.cancelOrder(1L));

        assertEquals("Order not found with id: 1", exception.getMessage());
        verify(orderRepository).existsById(1L);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(eventPublisher);
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

package com.delivery.mapper;

import com.delivery.dto.AvailableDriverDto;
import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.util.OrderStatus;
import com.delivery.util.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DispatcherMapperTest {
    private final DispatcherMapper mapper = Mappers.getMapper(DispatcherMapper.class);

    private Order order;
    private User client;
    private User driver;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        client = new User();
        client.setId(1L);
        client.setFirstName("Alice");
        client.setLastName("Smith");
        client.setEmail("alice@example.com");
        client.setPhone("1234567890");

        driver = new User();
        driver.setId(2L);
        driver.setFirstName("Bob");
        driver.setLastName("Johnson");

        vehicle = new Vehicle();
        vehicle.setId(10L);
        vehicle.setLicensePlate("XYZ-123");

        order = new Order();
        order.setId(100L);
        order.setFromAddress("Warehouse A");
        order.setToAddress("Customer B");
        order.setCargoType("Electronics");
        order.setCargoDescription("Laptops and Monitors");
        order.setWeightKg(new BigDecimal("120.5"));
        order.setComment("Handle with care");
        order.setPrice(new BigDecimal("2500.00"));
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setPickupTime(LocalDateTime.now().plusDays(1));
        order.setCreatedAt(LocalDateTime.now().minusDays(1));
        order.setUpdatedAt(LocalDateTime.now());

        order.setClient(client);
        order.setDriver(driver);
        order.setVehicle(vehicle);
    }

    @Test
    void testToDispatcherOrderDetailsDto() {
        DispatcherOrderDetailsDto dto = mapper.toDispatcherOrderDetailsDto(order);

        assertEquals(order.getId(), dto.getId());
        assertEquals("Alice Smith", dto.getClientName());
        assertEquals("alice@example.com", dto.getClientEmail());
        assertEquals("1234567890", dto.getClientPhoneNumber());
        assertEquals("Bob Johnson", dto.getDriverName());
        assertEquals(order.getVehicle().getLicensePlate(), dto.getLicensePlate());
        assertEquals(order.getPrice(), dto.getPrice());
        assertEquals(order.getPaymentMethod(), dto.getPaymentMethod());
    }

    @Test
    void testToDispatcherOrderDetailsDto_NullDriver() {
        order.setDriver(null);

        DispatcherOrderDetailsDto dto = mapper.toDispatcherOrderDetailsDto(order);

        assertNull(dto.getDriverId());
        assertNull(dto.getDriverName());
    }

    @Test
    void testToAvailableDriverDto() {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Ford");
        vehicle.setModel("Transit");
        vehicle.setLicensePlate("DRIVE-001");

        User driver = new User();
        driver.setId(42L);
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPhone("555-1234");
        driver.setEmail("john.doe@example.com");
        driver.setVehicle(vehicle);

        AvailableDriverDto dto = mapper.toAvailableDriverDto(driver);

        assertEquals(driver.getId(), dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("555-1234", dto.getPhone());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("Ford", dto.getVehicleBrand());
        assertEquals("Transit", dto.getVehicleModel());
        assertEquals("DRIVE-001", dto.getLicensePlate());
    }

    @Test
    void testToAvailableDriversDto() {
        User driver1 = new User();
        driver1.setId(1L);
        driver1.setFirstName("Tom");
        driver1.setLastName("Hardy");
        driver1.setEmail("tom@example.com");
        driver1.setPhone("9999");
        Vehicle v1 = new Vehicle();
        v1.setBrand("Toyota");
        v1.setModel("Hiace");
        v1.setLicensePlate("TOY-999");
        driver1.setVehicle(v1);

        User driver2 = new User();
        driver2.setId(2L);
        driver2.setFirstName("Emily");
        driver2.setLastName("Clark");
        driver2.setEmail("emily@example.com");
        driver2.setPhone("8888");
        Vehicle v2 = new Vehicle();
        v2.setBrand("Mercedes");
        v2.setModel("Sprinter");
        v2.setLicensePlate("MB-123");
        driver2.setVehicle(v2);

        List<AvailableDriverDto> result = mapper.toAvailableDriversDto(List.of(driver1, driver2));

        assertEquals(2, result.size());
        assertEquals("Tom", result.get(0).getFirstName());
        assertEquals("Emily", result.get(1).getFirstName());
        assertEquals("Sprinter", result.get(1).getVehicleModel());
    }

}

package com.delivery.mapper;

import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.entity.Order;
import com.delivery.util.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DriverMapperTest {
    private final DriverMapper driverMapper = Mappers.getMapper(DriverMapper.class);

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(101L);
        order.setFromAddress("Warehouse 9");
        order.setToAddress("Store 24");
        order.setStatus(OrderStatus.ACCEPTED);
        order.setPickupTime(LocalDateTime.of(2025, 7, 20, 10, 30));
        order.setPrice(new BigDecimal("500.75"));
    }

    @Test
    void testToListDriverOrders() {
        List<DriverOrderListItemDto> result = driverMapper.toListDriverOrders(List.of(order));

        assertNotNull(result);
        assertEquals(1, result.size());

        DriverOrderListItemDto dto = result.get(0);
        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getFromAddress(), dto.getFromAddress());
        assertEquals(order.getToAddress(), dto.getToAddress());
        assertEquals(order.getStatus(), dto.getStatus());
        assertEquals(order.getPickupTime(), dto.getPickupTime());
        assertEquals(order.getPrice(), dto.getPrice());
    }

    @Test
    void testToListDriverOrders_EmptyList() {
        List<DriverOrderListItemDto> result = driverMapper.toListDriverOrders(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}

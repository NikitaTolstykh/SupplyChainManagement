package com.delivery.mapper;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.entity.Order;
import com.delivery.util.DistanceCategory;
import com.delivery.util.OrderStatus;
import com.delivery.util.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    private OrderRequestDto requestDto;
    private Order order;

    @BeforeEach
    void setUp() {
        requestDto = new OrderRequestDto();
        requestDto.setFromAddress("Street A");
        requestDto.setToAddress("Street B");
        requestDto.setCargoType("Furniture");
        requestDto.setCargoDescription("Table and chairs");
        requestDto.setWeightKg(new BigDecimal("50.0"));
        requestDto.setComment("Fragile");
        requestDto.setDistanceCategory(DistanceCategory.LONG);
        requestDto.setPaymentMethod(PaymentMethod.CARD);
        requestDto.setPickupTime(LocalDateTime.now().plusDays(1));

        order = new Order();
        order.setId(1L);
        order.setFromAddress("Street A");
        order.setToAddress("Street B");
        order.setCargoType("Furniture");
        order.setCargoDescription("Table and chairs");
        order.setWeightKg(new BigDecimal("50.0"));
        order.setComment("Fragile");
        order.setPrice(new BigDecimal("120.0"));
        order.setDistanceCategory(DistanceCategory.LONG);
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentMethod(PaymentMethod.CARD);
        order.setPickupTime(LocalDateTime.now().plusDays(1));
        order.setCreatedAt(LocalDateTime.now().minusDays(1));
        order.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldMapRequestDtoToEntity() {
        Order entity = orderMapper.toEntity(requestDto);

        assertEquals(requestDto.getFromAddress(), entity.getFromAddress());
        assertEquals(requestDto.getToAddress(), entity.getToAddress());
        assertEquals(requestDto.getCargoType(), entity.getCargoType());
        assertEquals(requestDto.getCargoDescription(), entity.getCargoDescription());
        assertEquals(requestDto.getWeightKg(), entity.getWeightKg());
        assertEquals(requestDto.getComment(), entity.getComment());
        assertEquals(requestDto.getDistanceCategory(), entity.getDistanceCategory());
        assertEquals(requestDto.getPaymentMethod(), entity.getPaymentMethod());
        assertEquals(requestDto.getPickupTime(), entity.getPickupTime());
    }

    @Test
    void shouldMapOrderToDetailsDto() {
        OrderDetailsDto dto = orderMapper.toDetailsDto(order);

        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getFromAddress(), dto.getFromAddress());
        assertEquals(order.getToAddress(), dto.getToAddress());
        assertEquals(order.getCargoType(), dto.getCargoType());
        assertEquals(order.getCargoDescription(), dto.getCargoDescription());
        assertEquals(order.getWeightKg(), dto.getWeightKg());
        assertEquals(order.getComment(), dto.getComment());
        assertEquals(order.getPrice(), dto.getPrice());
        assertEquals(order.getDistanceCategory().name(), dto.getDistanceCategory());
        assertEquals(order.getStatus().name(), dto.getStatus());
        assertEquals(order.getPickupTime(), dto.getPickupTime());
        assertEquals(order.getCreatedAt(), dto.getCreatedAt());
        assertEquals(order.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(order.getPaymentMethod().name(), dto.getPaymentMethod());
    }

    @Test
    void shouldMapOrderListToListItemDto() {
        List<OrderListItemDto> dtoList = orderMapper.toListItemDto(List.of(order));
        OrderListItemDto dto = dtoList.get(0);

        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getFromAddress(), dto.getFromAddress());
        assertEquals(order.getToAddress(), dto.getToAddress());
        assertEquals(order.getStatus().name(), dto.getStatus());
        assertEquals(order.getPrice(), dto.getPrice());
        assertEquals(order.getCreatedAt(), dto.getCreatedAt());
    }
}

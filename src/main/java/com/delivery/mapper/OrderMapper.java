package com.delivery.mapper;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequestDto dto);

    OrderDetailsDto toDetailsDto(Order order);

    List<OrderListItemDto> toListItemDto(List<Order> orderList);
}

package com.delivery.mapper;

import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    List<DriverOrderListItemDto> toListDriverOrders(List<Order> orderList);

}

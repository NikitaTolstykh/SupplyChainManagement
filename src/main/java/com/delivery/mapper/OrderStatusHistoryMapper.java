package com.delivery.mapper;

import com.delivery.dto.OrderStatusHistoryDto;
import com.delivery.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderStatusHistoryMapper {
    @Mapping(source = "changedBy.email", target = "changedBy")
    OrderStatusHistoryDto toDto(OrderStatusHistory orderStatusHistory);

    List<OrderStatusHistoryDto> toDtoList(List<OrderStatusHistoryDto> orderStatusHistoryList);
}

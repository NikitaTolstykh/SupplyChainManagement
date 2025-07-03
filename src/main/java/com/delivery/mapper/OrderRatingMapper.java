package com.delivery.mapper;

import com.delivery.dto.DispatcherOrderRatingDto;
import com.delivery.dto.OrderRatingResponseDto;
import com.delivery.entity.OrderRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderRatingMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(expression = "java(orderRating.getClient().getFirstName() + \" \" + orderRating.getClient().getLastName())", target = "clientFullName")
    OrderRatingResponseDto toDto(OrderRating orderRating);

}

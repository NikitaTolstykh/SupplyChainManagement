package com.delivery.mapper;

import com.delivery.dto.AvailableDriverDto;
import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DispatcherMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(expression = "java(order.getClient().getFirstName() + \" \" + order.getClient().getLastName())", target = "clientName")
    @Mapping(source = "client.email", target = "clientEmail")
    @Mapping(source = "client.phone", target = "clientPhoneNumber")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(expression = "java(order.getDriver() != null ? order.getDriver().getFirstName() + \" \" + order.getDriver().getLastName() : null)", target = "driverName")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
    DispatcherOrderDetailsDto toDispatcherOrderDetailsDto(Order order);

    @Mapping(source = "vehicle.brand", target = "vehicleBrand")
    @Mapping(source = "vehicle.model", target = "vehicleModel")
    @Mapping(source = "vehicle.licensePlate", target = "licensePlate")
    AvailableDriverDto toAvailableDriverDto(User user);
    List<AvailableDriverDto> toAvailableDriversDto(List<User> availableDrives);



}

package com.delivery.mapper;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(source = "user.id", target = "driverId")
    VehicleDto vehicleToDto(Vehicle vehicle);

    @Mapping(target = "user", source = "user")
    Vehicle vehicleToEntity(VehicleDto vehicleDto);

    List<VehicleDto> vehicleListDto(List<Vehicle> vehicleListEntity);
}

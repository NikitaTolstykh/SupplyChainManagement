package com.delivery.service;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    List<VehicleDto> getAllVehicles();

    VehicleDto getVehicle(Long id);

    VehicleDto addVehicle(VehicleDto vehicleDto);

    VehicleDto editVehicle(Long id, VehicleDto vehicleDto);

    void deleteVehicle(Long id);

}

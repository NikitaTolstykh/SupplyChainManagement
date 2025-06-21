package com.delivery.service;

import com.delivery.dto.VehicleDto;

import java.util.List;

public interface VehicleService {
    List<VehicleDto> getAllVehicles();
    VehicleDto getVehicle();
    VehicleDto addVehicle();
    VehicleDto editVehicle(Long id, VehicleDto vehicleDto);
    void deleteVehicle(Long id);

}

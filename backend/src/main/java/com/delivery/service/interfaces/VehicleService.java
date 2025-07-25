package com.delivery.service.interfaces;

import com.delivery.dto.VehicleDto;

import java.util.List;

public interface VehicleService {
    List<VehicleDto> getAllVehicles();

    VehicleDto getVehicle(Long id);

    VehicleDto addVehicle(VehicleDto vehicleDto);

    VehicleDto editVehicle(Long id, VehicleDto vehicleDto);

    void deleteVehicle(Long id);

}

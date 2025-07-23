package com.delivery.util.updateData;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.validation.RoleValidator;
import org.springframework.stereotype.Component;

@Component
public class VehicleDataService {
    private final UserLookupService userLookupService;
    private final RoleValidator roleValidator;

    public VehicleDataService(UserLookupService userLookupService, RoleValidator roleValidator) {
        this.userLookupService = userLookupService;
        this.roleValidator = roleValidator;
    }

    public void updateVehicleFields(Vehicle vehicle, VehicleDto vehicleDto) {
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setComment(vehicleDto.getComment());
    }

    public void assignDriverToVehicle(Vehicle vehicle, User driver) {
        vehicle.setDriver(driver);
    }

    public void updateDriverIfChanged(Vehicle vehicle, long newDriverId) {
        if (!vehicle.getDriver().getId().equals(newDriverId)) {
            User newDriver = userLookupService.findUserById(newDriverId);
            roleValidator.validateDriverRole(newDriver.getRole());
            vehicle.setDriver(newDriver);
        }
    }

    public Vehicle createVehicleWithDriver(VehicleDto vehicleDto, User driver) {
        Vehicle vehicle = new Vehicle();
        updateVehicleFields(vehicle, vehicleDto);
        assignDriverToVehicle(vehicle, driver);
        return vehicle;
    }
}
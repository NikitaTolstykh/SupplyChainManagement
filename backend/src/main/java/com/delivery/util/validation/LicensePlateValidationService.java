package com.delivery.util.validation;

import com.delivery.entity.Vehicle;
import com.delivery.exception.LicensePlateAlreadyExistsException;
import com.delivery.repository.VehicleRepository;
import org.springframework.stereotype.Component;

@Component
public class LicensePlateValidationService {
    private final VehicleRepository vehicleRepository;

    public LicensePlateValidationService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void validateLicensePlateUniqueness(String licensePlate) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new LicensePlateAlreadyExistsException("Vehicle with license plate " + licensePlate + " already exists");
        }
    }

    public void validateLicensePlateForUpdate(Vehicle existingVehicle, String licensePlate) {
        if (!existingVehicle.getLicensePlate().equals(licensePlate)) {
            validateLicensePlateUniqueness(licensePlate);
        }
    }
}
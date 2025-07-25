package com.delivery.util.lookup;


import com.delivery.entity.Vehicle;
import com.delivery.exception.VehicleNotFoundException;
import com.delivery.repository.VehicleRepository;
import org.springframework.stereotype.Component;
@Component
public class VehicleLookupService {
    private final VehicleRepository vehicleRepository;

    public VehicleLookupService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with id " + id + " not found"));
    }
}

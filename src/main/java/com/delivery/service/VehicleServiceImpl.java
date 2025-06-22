package com.delivery.service;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.mapper.VehicleMapper;
import com.delivery.repository.UserRepository;
import com.delivery.repository.VehicleRepository;
import com.delivery.util.RoleValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper,
                              UserRepository userRepository, RoleValidator roleValidator) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
    }

    @Override
    public List<VehicleDto> getAllVehicles() {
        return vehicleMapper.vehicleListDto(vehicleRepository.findAll());
    }

    @Override
    public VehicleDto getVehicle(Long id) {
        Vehicle foundVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        return vehicleMapper.vehicleToDto(foundVehicle);
    }

    @Override
    public VehicleDto addVehicle(VehicleDto vehicleDto) {
        if (vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        User driver = userRepository.findById(vehicleDto.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        roleValidator.validateDriverRole(driver.getRole());

        Vehicle vehicle = vehicleMapper.vehicleToEntity(vehicleDto);
        vehicle.setDriver(driver);
        return vehicleMapper.vehicleToDto(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDto editVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle foundVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (!foundVehicle.getLicensePlate().equals(vehicleDto.getLicensePlate())
                && vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())) {
            throw new IllegalArgumentException("License plate already exists");
        }

        if (!foundVehicle.getDriver().getId().equals(vehicleDto.getDriverId())) {
            User newDriver = userRepository.findById(vehicleDto.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

            roleValidator.validateDriverRole(newDriver.getRole());

            foundVehicle.setDriver(newDriver);
        }
        foundVehicle.setBrand(vehicleDto.getBrand());
        foundVehicle.setModel(vehicleDto.getModel());
        foundVehicle.setColor(vehicleDto.getColor());
        foundVehicle.setLicensePlate(vehicleDto.getLicensePlate());
        foundVehicle.setComment(vehicleDto.getComment());

        Vehicle updatedVehicle = vehicleRepository.save(foundVehicle);
        return vehicleMapper.vehicleToDto(updatedVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }
}

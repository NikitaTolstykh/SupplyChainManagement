package com.delivery.service.impl;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.event.VehicleAssignedEvent;
import com.delivery.exception.DriverNotFoundException;
import com.delivery.exception.LicensePlateAlreadyExistsException;
import com.delivery.exception.VehicleNotFoundException;
import com.delivery.mapper.VehicleMapper;
import com.delivery.repository.UserRepository;
import com.delivery.repository.VehicleRepository;
import com.delivery.service.interfaces.VehicleService;
import com.delivery.util.validation.RoleValidator;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;
    private final ApplicationEventPublisher eventPublisher;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper,
                              UserRepository userRepository, RoleValidator roleValidator, ApplicationEventPublisher eventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public List<VehicleDto> getAllVehicles() {
        return vehicleMapper.vehicleListDto(vehicleRepository.findAll());
    }

    @Override
    @Transactional
    public VehicleDto getVehicle(Long id) {
        Vehicle vehicle = findVehicleById(id);
        return vehicleMapper.vehicleToDto(vehicle);
    }

    @Override
    @Transactional
    public VehicleDto addVehicle(VehicleDto vehicleDto) {
        validateLicensePlateUniqueness(vehicleDto.getLicensePlate());
        User driver = findAndValidateDriver(vehicleDto.getDriverId());

        Vehicle vehicle = vehicleMapper.vehicleToEntity(vehicleDto);
        vehicle.setDriver(driver);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        eventPublisher.publishEvent(new VehicleAssignedEvent(driver, savedVehicle));

        return vehicleMapper.vehicleToDto(savedVehicle);
    }

    @Override
    @Transactional
    public VehicleDto editVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle existingVehicle = findVehicleById(id);

        validateLicensePlateForUpdate(existingVehicle, vehicleDto.getLicensePlate());
        updateDriverIfChanged(existingVehicle, vehicleDto.getDriverId());
        updateVehicleFields(existingVehicle, vehicleDto);

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return vehicleMapper.vehicleToDto(updatedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.delete(findVehicleById(id));
    }

    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with id " + id + " not found"));
    }

    private User findAndValidateDriver(Long id) {
        User driver = userRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver with id " + id + " not found"));

        roleValidator.validateDriverRole(driver.getRole());

        return driver;
    }

    private void validateLicensePlateUniqueness(String licensePlate) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new LicensePlateAlreadyExistsException("Vehicle with license plate " + licensePlate + " already exists");
        }
    }

    private void validateLicensePlateForUpdate(Vehicle existingVehicle, String licensePlate) {
        if (!existingVehicle.getLicensePlate().equals(licensePlate)) {
            validateLicensePlateUniqueness(licensePlate);
        }
    }

    private void updateDriverIfChanged(Vehicle vehicle, long newDriverId) {
        if (!vehicle.getDriver().getId().equals(newDriverId)) {
            User newDriver = findAndValidateDriver(newDriverId);
            vehicle.setDriver(newDriver);
        }
    }

    private void updateVehicleFields(Vehicle vehicle, VehicleDto vehicleDto) {
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setComment(vehicleDto.getComment());
    }
}

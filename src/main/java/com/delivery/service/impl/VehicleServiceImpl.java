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
import com.delivery.util.changeData.VehicleDataService;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.lookup.VehicleLookupService;
import com.delivery.util.validation.LicensePlateValidationService;
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
    private final VehicleLookupService vehicleLookupService;
    private final LicensePlateValidationService licensePlateValidationService;
    private final VehicleDataService vehicleDataService;
    private final UserLookupService userLookupService;


    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper,
                              UserRepository userRepository, RoleValidator roleValidator,
                              ApplicationEventPublisher eventPublisher, VehicleLookupService vehicleLookupService,
                              LicensePlateValidationService licensePlateValidationService,
                              VehicleDataService vehicleDataService, UserLookupService userLookupService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.roleValidator = roleValidator;
        this.eventPublisher = eventPublisher;
        this.vehicleLookupService = vehicleLookupService;
        this.licensePlateValidationService = licensePlateValidationService;
        this.vehicleDataService = vehicleDataService;
        this.userLookupService = userLookupService;
    }

    @Override
    @Transactional
    public List<VehicleDto> getAllVehicles() {
        return vehicleMapper.vehicleListDto(vehicleRepository.findAll());
    }

    @Override
    @Transactional
    public VehicleDto getVehicle(Long id) {
        Vehicle vehicle = vehicleLookupService.findVehicleById(id);
        return vehicleMapper.vehicleToDto(vehicle);
    }

    @Override
    @Transactional
    public VehicleDto addVehicle(VehicleDto vehicleDto) {
        licensePlateValidationService.validateLicensePlateUniqueness(vehicleDto.getLicensePlate());
        User driver = userLookupService.findUserById(vehicleDto.getDriverId());

        roleValidator.validateDriverRole(driver.getRole());

        Vehicle vehicle = vehicleDataService.createVehicleWithDriver(vehicleDto, driver);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        eventPublisher.publishEvent(new VehicleAssignedEvent(driver, savedVehicle));

        return vehicleMapper.vehicleToDto(savedVehicle);
    }

    @Override
    @Transactional
    public VehicleDto editVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle existingVehicle = vehicleLookupService.findVehicleById(id);

        licensePlateValidationService.validateLicensePlateForUpdate(existingVehicle, vehicleDto.getLicensePlate());
        vehicleDataService.updateDriverIfChanged(existingVehicle, vehicleDto.getDriverId());
        vehicleDataService.updateVehicleFields(existingVehicle, vehicleDto);

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return vehicleMapper.vehicleToDto(updatedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.delete(vehicleLookupService.findVehicleById(id));
    }
}

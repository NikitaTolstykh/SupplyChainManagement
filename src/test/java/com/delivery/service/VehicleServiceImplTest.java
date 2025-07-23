package com.delivery.service;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import com.delivery.event.VehicleAssignedEvent;
import com.delivery.mapper.VehicleMapper;
import com.delivery.repository.VehicleRepository;
import com.delivery.service.impl.VehicleServiceImpl;
import com.delivery.util.Role;
import com.delivery.util.lookup.UserLookupService;
import com.delivery.util.lookup.VehicleLookupService;
import com.delivery.util.updateData.VehicleDataService;
import com.delivery.util.validation.LicensePlateValidationService;
import com.delivery.util.validation.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private VehicleLookupService vehicleLookupService;

    @Mock
    private LicensePlateValidationService licensePlateValidationService;

    @Mock
    private VehicleDataService vehicleDataService;

    @Mock
    private UserLookupService userLookupService;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;
    private VehicleDto vehicleDto;
    private User driver;

    @BeforeEach
    void setUp() {
        driver = new User();
        driver.setId(1L);
        driver.setRole(Role.DRIVER);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setColor("White");
        vehicle.setLicensePlate("ABC123");
        vehicle.setDriver(driver);

        vehicleDto = new VehicleDto();
        vehicleDto.setId(1L);
        vehicleDto.setBrand("Toyota");
        vehicleDto.setModel("Camry");
        vehicleDto.setColor("White");
        vehicleDto.setLicensePlate("ABC123");
        vehicleDto.setDriverId(driver.getId());
    }

    @Test
    void getAllVehicles_ShouldReturnListOfVehicleDto() {
        List<Vehicle> vehicles = List.of(vehicle);
        List<VehicleDto> vehicleDtos = List.of(vehicleDto);

        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.vehicleListDto(vehicles)).thenReturn(vehicleDtos);

        List<VehicleDto> result = vehicleService.getAllVehicles();

        assertEquals(1, result.size());
        verify(vehicleRepository).findAll();
        verify(vehicleMapper).vehicleListDto(vehicles);
    }

    @Test
    void getVehicle_ShouldReturnVehicleDto_WhenVehicleExists() {
        when(vehicleLookupService.findVehicleById(1L)).thenReturn(vehicle);
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehicleService.getVehicle(1L);

        assertNotNull(result);
        assertEquals(vehicleDto.getId(), result.getId());
        verify(vehicleLookupService).findVehicleById(1L);
        verify(vehicleMapper).vehicleToDto(vehicle);
    }
    @Test
    void addVehicle_ShouldSaveVehicleAndPublishEvent() {
        doNothing().when(licensePlateValidationService).validateLicensePlateUniqueness(vehicleDto.getLicensePlate());
        when(userLookupService.findUserById(driver.getId())).thenReturn(driver);
        doNothing().when(roleValidator).validateDriverRole(driver.getRole());
        when(vehicleDataService.createVehicleWithDriver(vehicleDto, driver)).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehicleService.addVehicle(vehicleDto);

        assertNotNull(result);
        assertEquals(vehicleDto.getLicensePlate(), result.getLicensePlate());
        verify(licensePlateValidationService).validateLicensePlateUniqueness(vehicleDto.getLicensePlate());
        verify(userLookupService).findUserById(driver.getId());
        verify(roleValidator).validateDriverRole(driver.getRole());
        verify(vehicleDataService).createVehicleWithDriver(vehicleDto, driver);
        verify(vehicleRepository).save(vehicle);
        verify(eventPublisher).publishEvent(any(VehicleAssignedEvent.class));
        verify(vehicleMapper).vehicleToDto(vehicle);
    }

    @Test
    void editVehicle_ShouldUpdateVehicle() {
        VehicleDto updatedDto = new VehicleDto();
        updatedDto.setBrand("Honda");
        updatedDto.setModel("Accord");
        updatedDto.setColor("Black");
        updatedDto.setLicensePlate("XYZ789");
        updatedDto.setDriverId(driver.getId());

        when(vehicleLookupService.findVehicleById(1L)).thenReturn(vehicle);
        doNothing().when(licensePlateValidationService).validateLicensePlateForUpdate(vehicle, updatedDto.getLicensePlate());
        doNothing().when(vehicleDataService).updateDriverIfChanged(vehicle, updatedDto.getDriverId());
        doNothing().when(vehicleDataService).updateVehicleFields(vehicle, updatedDto);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(updatedDto);

        VehicleDto result = vehicleService.editVehicle(1L, updatedDto);

        assertEquals(updatedDto.getBrand(), result.getBrand());
        verify(vehicleLookupService).findVehicleById(1L);
        verify(licensePlateValidationService).validateLicensePlateForUpdate(vehicle, updatedDto.getLicensePlate());
        verify(vehicleDataService).updateDriverIfChanged(vehicle, updatedDto.getDriverId());
        verify(vehicleDataService).updateVehicleFields(vehicle, updatedDto);
        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).vehicleToDto(vehicle);
    }

    @Test
    void deleteVehicle_ShouldDeleteVehicle_WhenVehicleExists() {
        when(vehicleLookupService.findVehicleById(1L)).thenReturn(vehicle);

        vehicleService.deleteVehicle(1L);

        verify(vehicleLookupService).findVehicleById(1L);
        verify(vehicleRepository).delete(vehicle);
    }
}
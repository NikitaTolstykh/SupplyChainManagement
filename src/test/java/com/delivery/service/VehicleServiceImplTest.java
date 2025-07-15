package com.delivery.service;

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
import com.delivery.util.Role;
import com.delivery.util.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

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
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehicleService.getVehicle(1L);

        assertNotNull(result);
        assertEquals(vehicleDto.getId(), result.getId());
    }

    @Test
    void getVehicle_ShouldThrowException_WhenVehicleNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> vehicleService.getVehicle(1L));
    }

    @Test
    void addVehicle_ShouldSaveVehicleAndPublishEvent() {
        when(vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())).thenReturn(false);
        when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        doNothing().when(roleValidator).validateDriverRole(driver.getRole());

        when(vehicleMapper.vehicleToEntity(vehicleDto)).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(vehicleDto);

        VehicleDto result = vehicleService.addVehicle(vehicleDto);

        assertNotNull(result);
        assertEquals(vehicleDto.getLicensePlate(), result.getLicensePlate());
        verify(eventPublisher).publishEvent(any(VehicleAssignedEvent.class));    }

    @Test
    void addVehicle_ShouldThrowException_WhenLicensePlateExists() {
        when(vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())).thenReturn(true);

        assertThrows(LicensePlateAlreadyExistsException.class, () -> vehicleService.addVehicle(vehicleDto));
    }

    @Test
    void addVehicle_ShouldThrowException_WhenDriverNotFound() {
        when(vehicleRepository.existsByLicensePlate(vehicleDto.getLicensePlate())).thenReturn(false);
        when(userRepository.findById(driver.getId())).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> vehicleService.addVehicle(vehicleDto));
    }

    @Test
    void editVehicle_ShouldUpdateVehicle() {
        VehicleDto updatedDto = new VehicleDto();
        updatedDto.setBrand("Honda");
        updatedDto.setModel("Accord");
        updatedDto.setColor("Black");
        updatedDto.setLicensePlate("XYZ789");
        updatedDto.setDriverId(driver.getId());

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.existsByLicensePlate(updatedDto.getLicensePlate())).thenReturn(false);
        when(userRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        doNothing().when(roleValidator).validateDriverRole(driver.getRole());
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.vehicleToDto(vehicle)).thenReturn(updatedDto);

        VehicleDto result = vehicleService.editVehicle(1L, updatedDto);

        assertEquals(updatedDto.getBrand(), result.getBrand());
    }

    @Test
    void deleteVehicle_ShouldDeleteVehicle_WhenVehicleExists() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void deleteVehicle_ShouldThrowException_WhenVehicleNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> vehicleService.deleteVehicle(1L));
    }

}

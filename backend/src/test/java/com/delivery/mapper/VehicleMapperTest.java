package com.delivery.mapper;

import com.delivery.dto.VehicleDto;
import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VehicleMapperTest {

    private final VehicleMapper vehicleMapper = Mappers.getMapper(VehicleMapper.class);

    private Vehicle vehicle;
    private VehicleDto vehicleDto;

    @BeforeEach
    void setUp() {
        User driver = new User();
        driver.setId(100L);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setColor("White");
        vehicle.setLicensePlate("XYZ-123");
        vehicle.setComment("No issues");
        vehicle.setDriver(driver);

        vehicleDto = new VehicleDto();
        vehicleDto.setId(1L);
        vehicleDto.setBrand("Toyota");
        vehicleDto.setModel("Corolla");
        vehicleDto.setColor("White");
        vehicleDto.setLicensePlate("XYZ-123");
        vehicleDto.setComment("No issues");
        vehicleDto.setDriverId(100L);
    }

    @Test
    void vehicleToDto_ShouldMapCorrectly() {
        VehicleDto dto = vehicleMapper.vehicleToDto(vehicle);

        assertEquals(vehicle.getId(), dto.getId());
        assertEquals(vehicle.getBrand(), dto.getBrand());
        assertEquals(vehicle.getModel(), dto.getModel());
        assertEquals(vehicle.getColor(), dto.getColor());
        assertEquals(vehicle.getLicensePlate(), dto.getLicensePlate());
        assertEquals(vehicle.getComment(), dto.getComment());
        assertEquals(vehicle.getDriver().getId(), dto.getDriverId());
    }

    @Test
    void vehicleToEntity_ShouldMapCorrectly_WithoutDriver() {
        Vehicle entity = vehicleMapper.vehicleToEntity(vehicleDto);

        assertEquals(vehicleDto.getId(), entity.getId());
        assertEquals(vehicleDto.getBrand(), entity.getBrand());
        assertEquals(vehicleDto.getModel(), entity.getModel());
        assertEquals(vehicleDto.getColor(), entity.getColor());
        assertEquals(vehicleDto.getLicensePlate(), entity.getLicensePlate());
        assertEquals(vehicleDto.getComment(), entity.getComment());
        assertNull(entity.getDriver()); // because driver is ignored in the mapper
    }

    @Test
    void vehicleListDto_ShouldMapListCorrectly() {
        List<VehicleDto> dtoList = vehicleMapper.vehicleListDto(List.of(vehicle));

        assertEquals(1, dtoList.size());
        assertEquals(vehicle.getLicensePlate(), dtoList.get(0).getLicensePlate());
        assertEquals(vehicle.getDriver().getId(), dtoList.get(0).getDriverId());
    }
}

package com.delivery.dto;

import com.delivery.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    private Long id;

    @NotBlank(message = "car must have a brand")
    private String brand;

    @NotBlank(message = "car must have a model")
    private String model;

    @NotBlank(message = "car must have color")
    private String color;

    @NotBlank(message = "car must have a license plate")
    private String licensePlate;

    private String comment;

    @NotNull(message = "car must have a driver(user_id)")
    private Long driverId;
}

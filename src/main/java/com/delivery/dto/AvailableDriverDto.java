package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDriverDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String vehicleBrand;
    private String vehicleModel;
    private String licensePlate;
}

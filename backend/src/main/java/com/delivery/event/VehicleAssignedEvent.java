package com.delivery.event;

import com.delivery.entity.User;
import com.delivery.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleAssignedEvent {
    private User driver;
    private Vehicle vehicle;
}

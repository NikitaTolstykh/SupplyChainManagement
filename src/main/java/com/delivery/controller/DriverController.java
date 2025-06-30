package com.delivery.controller;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
public class DriverController {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/orders/assigned")
    public ResponseEntity<List<DriverOrderListItemDto>> getAssignOrders() {
        String driverEmail = getCurrentUserEmail();
        return ResponseEntity.ok(driverService.getAssignedOrders(driverEmail));
    }

    @GetMapping("/orders/completed")
    public ResponseEntity<List<DriverOrderListItemDto>> getCompletedOrders() {
        String driverEmail = getCurrentUserEmail();
        return ResponseEntity.ok(driverService.getCompletedOrders(driverEmail));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<DispatcherOrderDetailsDto> getOrderDetails(@PathVariable Long id) {
        String driverEmail = getCurrentUserEmail();
        return ResponseEntity.ok(driverService.getOrderDetails(id, driverEmail));
    }


    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

package com.delivery.controller;

import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.DriverOrderListItemDto;
import com.delivery.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping ("/orders/{id}/accept")
    public ResponseEntity<Void> acceptOrder(@PathVariable Long id) {
        String driverEmail = getCurrentUserEmail();
        driverService.acceptOrder(id, driverEmail);
        return ResponseEntity.noContent().build();
    }


    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

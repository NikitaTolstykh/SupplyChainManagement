package com.delivery.controller;

import com.delivery.dto.AvailableDriverDto;
import com.delivery.dto.DispatcherOrderDetailsDto;
import com.delivery.dto.OrderListItemDto;
import com.delivery.service.DispatcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dispatcher")
public class DispatcherController {
    private final DispatcherService dispatcherService;

    public DispatcherController(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderListItemDto>> getAllOrders() {
        return ResponseEntity.ok(dispatcherService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<DispatcherOrderDetailsDto> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity.ok(dispatcherService.getOrderDetails(id));
    }

    @GetMapping("/available-drivers")
    public ResponseEntity<List<AvailableDriverDto>> getAvailableDrivers() {
        return ResponseEntity.ok(dispatcherService.availableDrivers());
    }
}

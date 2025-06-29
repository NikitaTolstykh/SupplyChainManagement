package com.delivery.controller;

import com.delivery.dto.*;
import com.delivery.service.DispatcherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/orders/{id}/assign-driver")
    public ResponseEntity<Void> assignDriver(@PathVariable Long id, @Valid @RequestBody AssignDriverRequestDto dto) {
        dispatcherService.assignDriver(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequestDto dto) {
        dispatcherService.updateOrderStatus(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Void> updateOrderInfo(@PathVariable Long id, @Valid @RequestBody OrderRequestDto dto) {
        dispatcherService.updateOrderInfo(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        dispatcherService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}

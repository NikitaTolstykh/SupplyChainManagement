package com.delivery.controller;

import com.delivery.dto.*;
import com.delivery.service.DispatcherService;
import com.delivery.service.OrderRatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatcher")
public class DispatcherController {
    private final DispatcherService dispatcherService;
    private final OrderRatingService orderRatingService;

    public DispatcherController(DispatcherService dispatcherService, OrderRatingService orderRatingService) {
        this.dispatcherService = dispatcherService;
        this.orderRatingService = orderRatingService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderListItemDto>> getAllOrders() {
        return ResponseEntity.ok(dispatcherService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<DispatcherOrderDetailsDto> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity.ok(dispatcherService.getOrderDetails(id));
    }

    @GetMapping("/orders/{id}/status-history")
    public ResponseEntity<List<OrderStatusHistoryDto>> getOrderStatusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(dispatcherService.getOrderStatusHistory(id));
    }

    @GetMapping("/available-drivers")
    public ResponseEntity<List<AvailableDriverDto>> getAvailableDrivers() {
        return ResponseEntity.ok(dispatcherService.availableDrivers());
    }

    @GetMapping("/orders/ratings")
    public ResponseEntity<List<OrderRatingResponseDto>> getOrderRatings() {
        return ResponseEntity.ok(orderRatingService.opinionList());
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

    @PatchMapping("/orders/{id}/cancel-order")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        dispatcherService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }


}

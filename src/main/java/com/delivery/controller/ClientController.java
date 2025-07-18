package com.delivery.controller;

import com.delivery.dto.*;
import com.delivery.service.ClientService;
import com.delivery.service.ClientStatisticsService;
import com.delivery.service.OrderRatingService;
import com.delivery.util.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/client/orders")
public class ClientController {
    private final ClientService clientService;
    private final OrderRatingService orderRatingService;
    private final ClientStatisticsService clientStatisticsService;
    private final CurrentUserService currentUserService;

    public ClientController(ClientService clientService, OrderRatingService orderRatingService
            , ClientStatisticsService clientStatisticsService, CurrentUserService currentUserService) {
        this.clientService = clientService;
        this.orderRatingService = orderRatingService;
        this.clientStatisticsService = clientStatisticsService;
        this.currentUserService = currentUserService;
    }

    // === Order MANAGEMENT ====

    @PostMapping
    public ResponseEntity<OrderDetailsDto> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.status(201).body(clientService.createOrder(dto, email));
    }

    @GetMapping
    public ResponseEntity<List<OrderListItemDto>> getClientOrders() {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.ok(clientService.getClientOrders(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable Long id) {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.ok(clientService.getOrderDetails(id, email));
    }

    @GetMapping("/{id}/status-history")
    public ResponseEntity<List<OrderStatusHistoryDto>> getOrderStatusHistory(@PathVariable Long id) {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.ok(clientService.getOrderStatusHistory(id, email));
    }

    // === RATE Order MANAGEMENT ====
    @PostMapping("/{id}/rating")
    public ResponseEntity<OrderRatingResponseDto> rateOrder(@PathVariable Long id, @Valid @RequestBody OrderRatingRequestDto dto) {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.status(201).body(orderRatingService.rateOrder(id, dto, email));
    }

    @GetMapping("/available-for-rating")
    public ResponseEntity<List<OrderListItemDto>> getOrdersAvailableForRating() {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.ok(clientService.getOrdersAvailableForRating(email));
    }

    // === STATISTICS ====

    @GetMapping("/statistics")
    public ResponseEntity<ClientStatisticsDto> getClientStatistics() {
        String email = currentUserService.getCurrentUserEmail();
        return ResponseEntity.ok(clientStatisticsService.getClientStatistics(email));
    }
}

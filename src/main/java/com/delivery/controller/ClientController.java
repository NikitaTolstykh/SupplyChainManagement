package com.delivery.controller;

import com.delivery.dto.OrderDetailsDto;
import com.delivery.dto.OrderRequestDto;
import com.delivery.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/client/orders")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<OrderDetailsDto> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        String email = getCurrentUserEmail();
        return ResponseEntity.status(201).body(clientService.createOrder(dto, email));
    }


    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

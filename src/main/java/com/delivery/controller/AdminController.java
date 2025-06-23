package com.delivery.controller;

import com.delivery.dto.UserRequestDto;
import com.delivery.dto.UserResponseDto;
import com.delivery.dto.VehicleDto;
import com.delivery.service.AdminService;
import com.delivery.service.VehicleService;
import com.delivery.util.Role;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final VehicleService vehicleService;
    private final AdminService adminService;

    public AdminController(VehicleService vehicleService, AdminService adminService) {
        this.vehicleService = vehicleService;
        this.adminService = adminService;
    }

    // === WORKERS MANAGEMENT ===
    @GetMapping("/workers")
    public ResponseEntity<List<UserResponseDto>> getAllWorkers() {
        return ResponseEntity.ok(adminService.getAllWorkers());
    }

    @GetMapping("/workers/role/{role}")
    public ResponseEntity<List<UserResponseDto>> getAllWorkersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(adminService.getAllWorkersByRole(role));
    }

    @GetMapping("/workers/search")
    public ResponseEntity<List<UserResponseDto>> searchWorkers(@RequestParam String query) {
        return ResponseEntity.ok(adminService.searchWorkers(query));
    }

    @GetMapping("/workers/{id}")
    public ResponseEntity<UserResponseDto> getWorker(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getWorker(id));
    }

    @PostMapping("/workers")
    public ResponseEntity<UserResponseDto> addWorker(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto createdWorked = adminService.addWorker(userDto);
        return ResponseEntity.status(201).body(createdWorked);
    }

    @PutMapping("/workers/{id}")
    public ResponseEntity<UserResponseDto> editWorker(@PathVariable Long id, @Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(adminService.editWorker(id, userDto));
    }

    @DeleteMapping("/workers/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        adminService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }

    // === CARS MANAGEMENT ====

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicle(id));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleDto> addVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        VehicleDto createdVehicle = vehicleService.addVehicle(vehicleDto);
        return ResponseEntity.status(201).body(createdVehicle);
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.editVehicle(id, vehicleDto));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

}

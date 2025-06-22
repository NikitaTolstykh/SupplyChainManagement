package com.delivery.repository;

import com.delivery.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByLicensePlate(String licensePlate);
}

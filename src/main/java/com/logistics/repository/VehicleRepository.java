package com.logistics.repository;

import com.logistics.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = 'AVAILABLE'")
    long countAvailableVehicles();

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = 'ON_ROUTE'")
    long countOnRouteVehicles();
}

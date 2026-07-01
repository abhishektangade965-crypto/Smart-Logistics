package com.logistics.repository;

import com.logistics.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    List<Shipment> findByStatus(Shipment.ShipmentStatus status);
    List<Shipment> findByOrderId(Long orderId);
    List<Shipment> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.status = 'IN_TRANSIT'")
    long countInTransit();

    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.estimatedDelivery < :today AND s.status NOT IN ('DELIVERED','CANCELLED','RETURNED')")
    long countDelayed(LocalDate today);

    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.status NOT IN ('DELIVERED','CANCELLED','RETURNED')")
    long countActiveShipments();
}

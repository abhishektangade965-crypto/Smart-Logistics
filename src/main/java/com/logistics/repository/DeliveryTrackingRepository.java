package com.logistics.repository;

import com.logistics.entity.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {
    List<DeliveryTracking> findByShipmentIdOrderByTimestampDesc(Long shipmentId);
    List<DeliveryTracking> findByShipmentTrackingNumber(String trackingNumber);
}

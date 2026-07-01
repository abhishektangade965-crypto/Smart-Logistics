package com.logistics.service;

import com.logistics.dto.ShipmentDto;
import com.logistics.entity.DeliveryTracking;
import com.logistics.entity.Shipment;

import java.util.List;

public interface ShipmentService {
    List<ShipmentDto> findAll();
    ShipmentDto findById(Long id);
    ShipmentDto findByTrackingNumber(String trackingNumber);
    ShipmentDto save(ShipmentDto dto);
    ShipmentDto update(Long id, ShipmentDto dto);
    void delete(Long id);
    ShipmentDto updateStatus(Long id, Shipment.ShipmentStatus status);
    List<DeliveryTracking> getTrackingEvents(Long shipmentId);
    DeliveryTracking addTrackingEvent(Long shipmentId, DeliveryTracking event);
    String generateTrackingNumber();
    List<ShipmentDto> findRecent(int limit);
}

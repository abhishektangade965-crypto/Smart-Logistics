package com.logistics.rest;

import com.logistics.dto.ShipmentDto;
import com.logistics.entity.DeliveryTracking;
import com.logistics.entity.Shipment;
import com.logistics.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentRestController {

    private final ShipmentService service;

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> getAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getById(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ShipmentDto> track(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(service.findByTrackingNumber(trackingNumber));
    }

    @PostMapping
    public ResponseEntity<ShipmentDto> create(@Valid @RequestBody ShipmentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDto> update(@PathVariable Long id, @Valid @RequestBody ShipmentDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentDto> updateStatus(@PathVariable Long id,
                                                      @RequestParam Shipment.ShipmentStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/{id}/tracking")
    public ResponseEntity<List<DeliveryTracking>> getTracking(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTrackingEvents(id));
    }

    @PostMapping("/{id}/tracking")
    public ResponseEntity<DeliveryTracking> addTracking(@PathVariable Long id,
                                                         @RequestBody DeliveryTracking event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTrackingEvent(id, event));
    }
}

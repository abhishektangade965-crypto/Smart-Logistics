package com.logistics.serviceimpl;

import com.logistics.dto.ShipmentDto;
import com.logistics.entity.DeliveryTracking;
import com.logistics.entity.Shipment;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.*;
import com.logistics.service.ShipmentService;
import com.logistics.util.ShipmentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository repo;
    private final CustomerOrderRepository orderRepo;
    private final VehicleRepository vehicleRepo;
    private final RouteRepository routeRepo;
    private final DeliveryTrackingRepository trackingRepo;
    private final ShipmentEventPublisher eventPublisher;

    @Override @Transactional(readOnly = true)
    public List<ShipmentDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public ShipmentDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Shipment", "id", id)));
    }

    @Override @Transactional(readOnly = true)
    public ShipmentDto findByTrackingNumber(String trackingNumber) {
        return toDto(repo.findByTrackingNumber(trackingNumber).orElseThrow(() ->
                new ResourceNotFoundException("Shipment", "trackingNumber", trackingNumber)));
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public ShipmentDto save(ShipmentDto dto) {
        Shipment s = new Shipment();
        s.setTrackingNumber(generateTrackingNumber());
        copyFields(dto, s);
        ShipmentDto saved = toDto(repo.save(s));
        eventPublisher.publishStatusChange(saved.getTrackingNumber(), saved.getStatus() != null ? saved.getStatus().name() : null, saved.getDestinationAddress(), saved.getDriverName());
        return saved;
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public ShipmentDto update(Long id, ShipmentDto dto) {
        Shipment s = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Shipment", "id", id));
        copyFields(dto, s);
        ShipmentDto updated = toDto(repo.save(s));
        eventPublisher.publishStatusChange(updated.getTrackingNumber(), updated.getStatus() != null ? updated.getStatus().name() : null, updated.getDestinationAddress(), updated.getDriverName());
        return updated;
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Shipment", "id", id);
        repo.deleteById(id);
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public ShipmentDto updateStatus(Long id, Shipment.ShipmentStatus status) {
        Shipment s = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Shipment", "id", id));
        s.setStatus(status);
        ShipmentDto updated = toDto(repo.save(s));
        eventPublisher.publishStatusChange(updated.getTrackingNumber(), updated.getStatus() != null ? updated.getStatus().name() : null, updated.getDestinationAddress(), updated.getDriverName());
        return updated;
    }

    @Override @Transactional(readOnly = true)
    public List<DeliveryTracking> getTrackingEvents(Long shipmentId) {
        return trackingRepo.findByShipmentIdOrderByTimestampDesc(shipmentId);
    }

    @Override
    public DeliveryTracking addTrackingEvent(Long shipmentId, DeliveryTracking event) {
        Shipment shipment = repo.findById(shipmentId).orElseThrow(() ->
                new ResourceNotFoundException("Shipment", "id", shipmentId));
        event.setShipment(shipment);
        return trackingRepo.save(event);
    }

    @Override
    public String generateTrackingNumber() {
        String prefix = "SHP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        return prefix + "-" + (int)(Math.random() * 9000 + 1000);
    }

    @Override @Transactional(readOnly = true)
    public List<ShipmentDto> findRecent(int limit) {
        return repo.findTop10ByOrderByCreatedAtDesc().stream()
                .limit(limit).map(this::toDto).collect(Collectors.toList());
    }

    private void copyFields(ShipmentDto dto, Shipment s) {
        if (dto.getOrderId() != null) orderRepo.findById(dto.getOrderId()).ifPresent(s::setOrder);
        if (dto.getVehicleId() != null) vehicleRepo.findById(dto.getVehicleId()).ifPresent(s::setVehicle);
        if (dto.getRouteId() != null) routeRepo.findById(dto.getRouteId()).ifPresent(s::setRoute);
        s.setStatus(dto.getStatus() != null ? dto.getStatus() : Shipment.ShipmentStatus.PENDING);
        s.setOriginAddress(dto.getOriginAddress());
        s.setDestinationAddress(dto.getDestinationAddress());
        s.setScheduledDate(dto.getScheduledDate());
        s.setEstimatedDelivery(dto.getEstimatedDelivery());
        s.setWeight(dto.getWeight());
        s.setShippingCost(dto.getShippingCost());
        s.setDriverName(dto.getDriverName());
        s.setDriverPhone(dto.getDriverPhone());
        s.setNotes(dto.getNotes());
    }

    private ShipmentDto toDto(Shipment s) {
        return ShipmentDto.builder()
                .id(s.getId()).trackingNumber(s.getTrackingNumber())
                .orderId(s.getOrder() != null ? s.getOrder().getId() : null)
                .orderNumber(s.getOrder() != null ? s.getOrder().getOrderNumber() : null)
                .vehicleId(s.getVehicle() != null ? s.getVehicle().getId() : null)
                .vehiclePlate(s.getVehicle() != null ? s.getVehicle().getLicensePlate() : null)
                .routeId(s.getRoute() != null ? s.getRoute().getId() : null)
                .routeName(s.getRoute() != null ? s.getRoute().getName() : null)
                .status(s.getStatus()).originAddress(s.getOriginAddress())
                .destinationAddress(s.getDestinationAddress())
                .scheduledDate(s.getScheduledDate()).estimatedDelivery(s.getEstimatedDelivery())
                .actualDeliveryDate(s.getActualDeliveryDate())
                .weight(s.getWeight()).shippingCost(s.getShippingCost())
                .driverName(s.getDriverName()).driverPhone(s.getDriverPhone())
                .notes(s.getNotes()).createdAt(s.getCreatedAt()).build();
    }
}

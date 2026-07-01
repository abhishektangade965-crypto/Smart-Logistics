package com.logistics.dto;

import com.logistics.entity.Shipment;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShipmentDto {
    private Long id;
    private String trackingNumber;
    private Long orderId;
    private String orderNumber;
    private Long vehicleId;
    private String vehiclePlate;
    private Long routeId;
    private String routeName;
    private Shipment.ShipmentStatus status;
    private String originAddress;
    private String destinationAddress;
    private LocalDate scheduledDate;
    private LocalDate estimatedDelivery;
    private LocalDate actualDeliveryDate;
    private BigDecimal weight;
    private BigDecimal shippingCost;
    private String driverName;
    private String driverPhone;
    private String notes;
    private LocalDateTime createdAt;
}

package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shipments")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @Column(length = 300)
    private String originAddress;

    @Column(length = 300)
    private String destinationAddress;

    private LocalDate scheduledDate;
    private LocalDate actualPickupDate;
    private LocalDate estimatedDelivery;
    private LocalDate actualDeliveryDate;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal weight;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal volume;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(12,2)")
    private BigDecimal shippingCost;

    @Column(length = 100)
    private String driverName;

    @Column(length = 20)
    private String driverPhone;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<DeliveryTracking> trackingEvents;

    public enum ShipmentStatus {
        PENDING, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, FAILED, RETURNED, CANCELLED
    }
}

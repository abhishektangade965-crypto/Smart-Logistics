package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_tracking")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class DeliveryTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingStatus status;

    @Column(nullable = false, length = 300)
    private String location;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String updatedBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime timestamp;

    public enum TrackingStatus {
        ORDER_PLACED, PROCESSING, PICKED_UP, IN_TRANSIT, ARRIVED_HUB,
        OUT_FOR_DELIVERY, DELIVERY_ATTEMPTED, DELIVERED, EXCEPTION, RETURNED
    }
}

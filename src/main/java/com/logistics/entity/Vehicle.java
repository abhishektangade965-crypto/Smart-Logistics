package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String licensePlate;

    @Column(nullable = false, length = 100)
    private String make;

    @Column(length = 100)
    private String model;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal maxLoadCapacity;

    @Column(length = 50)
    private String capacityUnit;

    @Column(length = 100)
    private String driverName;

    @Column(length = 20)
    private String driverPhone;

    @Column(length = 150)
    private String driverLicense;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private LocalDate insuranceExpiry;
    private LocalDate registrationExpiry;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(12,2)")
    private BigDecimal totalMileage;

    @Column(length = 200)
    private String gpsTrackerId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum VehicleType {
        TRUCK, VAN, MOTORCYCLE, PICKUP, CONTAINER, REFRIGERATED, HEAVY_DUTY
    }
    public enum VehicleStatus {
        AVAILABLE, ON_ROUTE, IN_MAINTENANCE, RETIRED, UNAVAILABLE
    }
}

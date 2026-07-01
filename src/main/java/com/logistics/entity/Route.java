package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String origin;

    @Column(nullable = false, length = 200)
    private String destination;

    @Column(columnDefinition = "TEXT")
    private String waypoints;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal distanceKm;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(6,2)")
    private BigDecimal estimatedHours;

    // Fixed for Oracle Dialect compatibility
    @Column(columnDefinition = "DECIMAL(12,2)")
    private BigDecimal tollCost;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum Status { ACTIVE, INACTIVE }
}

package com.logistics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "warehouses")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 20)
    private String code;

    @Column(length = 300)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String zipCode;

    @Column(length = 100)
    private String managerName;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String email;

    // Changed to BigDecimal to resolve Oracle/Hibernate 6 size/scale mismatches
    @Min(0)
    @Column(precision = 12, scale = 2)
    private BigDecimal totalCapacity;

    @Min(0)
    @Builder.Default
    @Column(precision = 12, scale = 2)
    private BigDecimal usedCapacity = BigDecimal.ZERO;

    @Column(length = 50)
    private String capacityUnit;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "warehouse")
    private List<Inventory> inventoryList;

    // Updated math operation to safely use BigDecimal for division
    public Double getUtilizationPercent() {
        if (totalCapacity == null || totalCapacity.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return usedCapacity.divide(totalCapacity, 4, RoundingMode.HALF_UP)
                           .multiply(BigDecimal.valueOf(100))
                           .doubleValue();
    }

    public enum Status { ACTIVE, INACTIVE, MAINTENANCE }
}

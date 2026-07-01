package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Builder.Default
    private Integer quantityOnHand = 0;

    @Builder.Default
    private Integer quantityReserved = 0;

    @Builder.Default
    private Integer quantityOnOrder = 0;

    @Column(length = 100)
    private String binLocation;

    @Column(precision = 12, scale = 2)
    private BigDecimal averageCost;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    public Integer getAvailableQuantity() {
        return quantityOnHand - quantityReserved;
    }

    public boolean isLowStock() {
        return getAvailableQuantity() <= (product != null ? product.getReorderPoint() : 10);
    }
}

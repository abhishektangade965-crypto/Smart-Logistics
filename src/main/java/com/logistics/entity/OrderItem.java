package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @PrePersist
    @PreUpdate
    public void calculateLineTotal() {
        if (unitPrice != null && quantity != null) {
            BigDecimal gross = unitPrice.multiply(BigDecimal.valueOf(quantity));
            lineTotal = gross.subtract(discount != null ? discount : BigDecimal.ZERO);
        }
    }
}

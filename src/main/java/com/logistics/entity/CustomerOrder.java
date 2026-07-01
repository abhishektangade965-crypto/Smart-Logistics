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
@Table(name = "customer_orders")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String orderNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // Fixed column definition formatting for Oracle fixed-point numeric alignment
    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(length = 300)
    private String shippingAddress;

    @Column(length = 100)
    private String shippingMethod;

    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 100)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Shipment> shipments;

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, PACKED, SHIPPED, DELIVERED, CANCELLED, RETURNED
    }
    public enum PaymentStatus {
        PENDING, PAID, PARTIAL, OVERDUE, REFUNDED
    }
}

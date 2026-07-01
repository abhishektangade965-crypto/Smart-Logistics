package com.logistics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String sku;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Fixed column definition formatting for Oracle fixed-point numeric alignment
    @DecimalMin("0.0")
    @Column(nullable = false, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal unitPrice;

    @DecimalMin("0.0")
    @Column(columnDefinition = "DECIMAL(12,2)")
    private BigDecimal costPrice;

    @Column(length = 50)
    private String unit;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 100)
    private String brand;

    @Min(0)
    @Builder.Default
    private Integer reorderPoint = 10;

    @Min(0)
    @Builder.Default
    private Integer reorderQuantity = 50;

    @DecimalMin("0.0")
    @Column(columnDefinition = "DECIMAL(6,2)")
    private BigDecimal weight;

    @Column(length = 100)
    private String dimensions;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum Status { ACTIVE, INACTIVE, DISCONTINUED }
}

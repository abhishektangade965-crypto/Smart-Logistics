package com.logistics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 20)
    private String code;

    @Email
    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 300)
    private String billingAddress;

    @Column(length = 300)
    private String shippingAddress;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String zipCode;

    @Column(length = 100)
    private String contactPerson;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerType customerType = CustomerType.RETAIL;

    // Fixed column definition formatting for Oracle fixed-point numeric alignment
    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(columnDefinition = "DECIMAL(12,2)")
    @Builder.Default
    private BigDecimal totalRevenue = BigDecimal.ZERO;

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

    @OneToMany(mappedBy = "customer")
    private List<CustomerOrder> orders;

    public enum CustomerType { RETAIL, WHOLESALE, CORPORATE, VIP }
    public enum Status { ACTIVE, INACTIVE, SUSPENDED }
}

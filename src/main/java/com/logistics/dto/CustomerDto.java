package com.logistics.dto;

import com.logistics.entity.Customer;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerDto {
    private Long id;
    @NotBlank private String name;
    private String code;
    @Email private String email;
    private String phone;
    private String billingAddress;
    private String shippingAddress;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String contactPerson;
    private Customer.CustomerType customerType;
    private BigDecimal creditLimit;
    private BigDecimal totalRevenue;
    private Customer.Status status;
    private String notes;
}

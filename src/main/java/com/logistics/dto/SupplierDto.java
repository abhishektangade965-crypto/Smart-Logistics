package com.logistics.dto;

import com.logistics.entity.Supplier;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SupplierDto {
    private Long id;
    @NotBlank private String name;
    private String code;
    @Email private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String contactPerson;
    private String website;
    private Supplier.Status status;
    private String notes;
}

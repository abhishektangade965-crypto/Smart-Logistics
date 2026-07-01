package com.logistics.dto;

import com.logistics.entity.Warehouse;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WarehouseDto {
    private Long id;
    @NotBlank private String name;
    private String code;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String managerName;
    private String phone;
    private String email;
    @Min(0) private Double totalCapacity;
    private Double usedCapacity;
    private String capacityUnit;
    private Warehouse.Status status;
    private String notes;
    private Double utilizationPercent;
}

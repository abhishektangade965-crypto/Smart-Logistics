package com.logistics.dto;

import com.logistics.entity.Vehicle;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleDto {
    private Long id;
    private String licensePlate;
    private String make;
    private String model;
    private Integer year;
    private Vehicle.VehicleType type;
    private BigDecimal maxLoadCapacity;
    private String capacityUnit;
    private String driverName;
    private String driverPhone;
    private String driverLicense;
    private Vehicle.VehicleStatus status;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private LocalDate insuranceExpiry;
    private BigDecimal totalMileage;
    private String notes;
}

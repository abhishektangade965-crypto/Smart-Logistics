package com.logistics.service;

import com.logistics.dto.VehicleDto;
import com.logistics.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    List<VehicleDto> findAll();
    VehicleDto findById(Long id);
    VehicleDto save(VehicleDto dto);
    VehicleDto update(Long id, VehicleDto dto);
    void delete(Long id);
    List<VehicleDto> findByStatus(Vehicle.VehicleStatus status);
    VehicleDto updateStatus(Long id, Vehicle.VehicleStatus status);
}

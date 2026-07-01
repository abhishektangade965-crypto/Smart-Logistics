package com.logistics.service;

import com.logistics.dto.WarehouseDto;

import java.util.List;

public interface WarehouseService {
    List<WarehouseDto> findAll();
    WarehouseDto findById(Long id);
    WarehouseDto save(WarehouseDto dto);
    WarehouseDto update(Long id, WarehouseDto dto);
    void delete(Long id);
    List<WarehouseDto> findActive();
}

package com.logistics.service;

import com.logistics.dto.SupplierDto;

import java.util.List;

public interface SupplierService {
    List<SupplierDto> findAll();
    SupplierDto findById(Long id);
    SupplierDto save(SupplierDto dto);
    SupplierDto update(Long id, SupplierDto dto);
    void delete(Long id);
    List<SupplierDto> search(String query);
    List<SupplierDto> findByStatus(String status);
}

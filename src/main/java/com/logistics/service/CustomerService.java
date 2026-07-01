package com.logistics.service;

import com.logistics.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> findAll();
    CustomerDto findById(Long id);
    CustomerDto save(CustomerDto dto);
    CustomerDto update(Long id, CustomerDto dto);
    void delete(Long id);
    List<CustomerDto> search(String query);
}

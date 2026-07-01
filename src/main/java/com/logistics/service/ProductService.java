package com.logistics.service;

import com.logistics.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();
    Page<ProductDto> findAllPaginated(Pageable pageable);
    ProductDto findById(Long id);
    ProductDto findBySku(String sku);
    ProductDto save(ProductDto dto);
    ProductDto update(Long id, ProductDto dto);
    void delete(Long id);
    List<ProductDto> search(String query);
    List<ProductDto> findByCategory(Long categoryId);
    List<ProductDto> findBySupplier(Long supplierId);
}

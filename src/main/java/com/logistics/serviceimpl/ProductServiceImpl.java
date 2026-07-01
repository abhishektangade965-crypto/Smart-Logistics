package com.logistics.serviceimpl;

import com.logistics.dto.ProductDto;
import com.logistics.entity.Product;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CategoryRepository;
import com.logistics.repository.ProductRepository;
import com.logistics.repository.SupplierRepository;
import com.logistics.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;

    @Override @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaginated(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDto);
    }

    @Override @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product", "id", id)));
    }

    @Override @Transactional(readOnly = true)
    public ProductDto findBySku(String sku) {
        return toDto(repo.findBySku(sku).orElseThrow(() ->
                new ResourceNotFoundException("Product", "sku", sku)));
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public ProductDto save(ProductDto dto) {
        Product p = new Product();
        copyFields(dto, p);
        return toDto(repo.save(p));
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public ProductDto update(Long id, ProductDto dto) {
        Product p = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product", "id", id));
        copyFields(dto, p);
        return toDto(repo.save(p));
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardStats", allEntries = true)
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Product", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<ProductDto> search(String query) {
        return repo.search(query).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<ProductDto> findByCategory(Long categoryId) {
        return repo.findByCategoryId(categoryId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<ProductDto> findBySupplier(Long supplierId) {
        return repo.findBySupplierId(supplierId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private void copyFields(ProductDto dto, Product p) {
        p.setName(dto.getName());
        p.setSku(dto.getSku());
        p.setDescription(dto.getDescription());
        p.setUnitPrice(dto.getUnitPrice());
        p.setCostPrice(dto.getCostPrice());
        p.setUnit(dto.getUnit());
        p.setBrand(dto.getBrand());
        p.setReorderPoint(dto.getReorderPoint() != null ? dto.getReorderPoint() : 10);
        p.setReorderQuantity(dto.getReorderQuantity() != null ? dto.getReorderQuantity() : 50);
        p.setWeight(dto.getWeight());
        p.setDimensions(dto.getDimensions());
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : Product.Status.ACTIVE);
        if (dto.getCategoryId() != null) {
            categoryRepo.findById(dto.getCategoryId()).ifPresent(p::setCategory);
        }
        if (dto.getSupplierId() != null) {
            supplierRepo.findById(dto.getSupplierId()).ifPresent(p::setSupplier);
        }
    }

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId()).name(p.getName()).sku(p.getSku())
                .description(p.getDescription()).unitPrice(p.getUnitPrice())
                .costPrice(p.getCostPrice()).unit(p.getUnit()).brand(p.getBrand())
                .reorderPoint(p.getReorderPoint()).reorderQuantity(p.getReorderQuantity())
                .weight(p.getWeight()).dimensions(p.getDimensions()).status(p.getStatus())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .supplierName(p.getSupplier() != null ? p.getSupplier().getName() : null)
                .build();
    }
}

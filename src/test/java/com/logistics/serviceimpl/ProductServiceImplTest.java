package com.logistics.serviceimpl;

import com.logistics.dto.ProductDto;
import com.logistics.entity.Category;
import com.logistics.entity.Product;
import com.logistics.entity.Supplier;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CategoryRepository;
import com.logistics.repository.ProductRepository;
import com.logistics.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private Category category;
    private Supplier supplier;

    @BeforeEach
    public void setup() {
        category = Category.builder().id(1L).name("Electronics").code("ELEC").build();
        supplier = Supplier.builder().id(1L).name("TechCorp").code("TC001").build();

        product = new Product();
        product.setId(1L);
        product.setName("Smartphone");
        product.setSku("PHN-001");
        product.setUnitPrice(new BigDecimal("999.99"));
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setStatus(Product.Status.ACTIVE);

        productDto = ProductDto.builder()
                .id(1L)
                .name("Smartphone")
                .sku("PHN-001")
                .unitPrice(new BigDecimal("999.99"))
                .categoryId(1L)
                .supplierId(1L)
                .status(Product.Status.ACTIVE)
                .build();
    }

    @Test
    public void testFindById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDto found = productService.findById(1L);

        assertNotNull(found);
        assertEquals("Smartphone", found.getName());
        assertEquals("PHN-001", found.getSku());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(2L);
        });

        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    public void testSave_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto saved = productService.save(productDto);

        assertNotNull(saved);
        assertEquals("Smartphone", saved.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDelete_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete_NotFound() {
        when(productRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(2L);
        });

        verify(productRepository, times(1)).existsById(2L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}

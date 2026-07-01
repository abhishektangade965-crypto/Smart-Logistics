package com.logistics.dto;

import com.logistics.entity.Product;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDto {
    private Long id;
    @NotBlank private String name;
    @NotBlank private String sku;
    private String description;
    @NotNull @DecimalMin("0.0") private BigDecimal unitPrice;
    private BigDecimal costPrice;
    private String unit;
    private String brand;
    private Integer reorderPoint;
    private Integer reorderQuantity;
    private BigDecimal weight;
    private String dimensions;
    private Product.Status status;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
}

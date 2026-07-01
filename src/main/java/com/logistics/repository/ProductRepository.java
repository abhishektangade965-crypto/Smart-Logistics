package com.logistics.repository;

import com.logistics.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByStatus(Product.Status status);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySupplierId(Long supplierId);
    boolean existsBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:q% OR p.sku LIKE %:q%")
    List<Product> search(@Param("q") String query);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = 'ACTIVE'")
    long countActiveProducts();

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :catId AND p.status = 'ACTIVE'")
    List<Product> findActiveByCategoryId(@Param("catId") Long catId);
}

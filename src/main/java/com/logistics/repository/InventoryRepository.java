package com.logistics.repository;

import com.logistics.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<Inventory> findByWarehouseId(Long warehouseId);
    List<Inventory> findByProductId(Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand <= i.product.reorderPoint")
    List<Inventory> findLowStock();

    @Query("SELECT SUM(i.quantityOnHand * i.product.costPrice) FROM Inventory i WHERE i.product.costPrice IS NOT NULL")
    BigDecimal calculateTotalInventoryValue();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand <= i.product.reorderPoint")
    long countLowStockItems();

    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p JOIN FETCH i.warehouse w WHERE w.id = :warehouseId")
    List<Inventory> findByWarehouseIdWithDetails(@Param("warehouseId") Long warehouseId);
}

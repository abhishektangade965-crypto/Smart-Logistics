package com.logistics.service;

import com.logistics.entity.Inventory;
import com.logistics.entity.StockMovement;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    List<Inventory> findAll();
    Inventory findById(Long id);
    Inventory findByProductAndWarehouse(Long productId, Long warehouseId);
    Inventory save(Inventory inventory);
    void delete(Long id);
    List<Inventory> findLowStock();
    List<StockMovement> findAllMovements();
    StockMovement recordMovement(StockMovement movement);
    Map<String, Object> getInventoryStats();
}

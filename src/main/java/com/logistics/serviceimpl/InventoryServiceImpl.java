package com.logistics.serviceimpl;

import com.logistics.entity.Inventory;
import com.logistics.entity.StockMovement;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.InventoryRepository;
import com.logistics.repository.StockMovementRepository;
import com.logistics.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final StockMovementRepository movementRepo;

    @Override @Transactional(readOnly = true)
    public List<Inventory> findAll() { return inventoryRepo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Inventory findById(Long id) {
        return inventoryRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Inventory", "id", id));
    }

    @Override @Transactional(readOnly = true)
    public Inventory findByProductAndWarehouse(Long productId, Long warehouseId) {
        return inventoryRepo.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found"));
    }

    @Override
    public Inventory save(Inventory inventory) { return inventoryRepo.save(inventory); }

    @Override
    public void delete(Long id) {
        if (!inventoryRepo.existsById(id)) throw new ResourceNotFoundException("Inventory", "id", id);
        inventoryRepo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<Inventory> findLowStock() { return inventoryRepo.findLowStock(); }

    @Override @Transactional(readOnly = true)
    public List<StockMovement> findAllMovements() { return movementRepo.findAll(); }

    @Override
    public StockMovement recordMovement(StockMovement movement) {
        StockMovement saved = movementRepo.save(movement);
        // Adjust inventory
        if (movement.getToWarehouse() != null && movement.getProduct() != null) {
            inventoryRepo.findByProductIdAndWarehouseId(
                    movement.getProduct().getId(), movement.getToWarehouse().getId())
                .ifPresent(inv -> {
                    inv.setQuantityOnHand(inv.getQuantityOnHand() + movement.getQuantity());
                    inventoryRepo.save(inv);
                });
        }
        if (movement.getFromWarehouse() != null && movement.getProduct() != null
                && movement.getMovementType() == StockMovement.MovementType.OUTBOUND) {
            inventoryRepo.findByProductIdAndWarehouseId(
                    movement.getProduct().getId(), movement.getFromWarehouse().getId())
                .ifPresent(inv -> {
                    inv.setQuantityOnHand(Math.max(0, inv.getQuantityOnHand() - movement.getQuantity()));
                    inventoryRepo.save(inv);
                });
        }
        return saved;
    }

    @Override @Transactional(readOnly = true)
    public Map<String, Object> getInventoryStats() {
        Map<String, Object> stats = new HashMap<>();
        BigDecimal value = inventoryRepo.calculateTotalInventoryValue();
        stats.put("totalValue", value != null ? value : BigDecimal.ZERO);
        stats.put("lowStockCount", inventoryRepo.countLowStockItems());
        stats.put("totalItems", inventoryRepo.count());
        return stats;
    }
}

package com.logistics.rest;

import com.logistics.entity.Inventory;
import com.logistics.entity.StockMovement;
import com.logistics.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryRestController {

    private final InventoryService service;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getById(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStock() { return ResponseEntity.ok(service.findLowStock()); }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() { return ResponseEntity.ok(service.getInventoryStats()); }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovement>> getMovements() { return ResponseEntity.ok(service.findAllMovements()); }

    @PostMapping("/movements")
    public ResponseEntity<StockMovement> recordMovement(@RequestBody StockMovement movement) {
        return ResponseEntity.ok(service.recordMovement(movement));
    }
}

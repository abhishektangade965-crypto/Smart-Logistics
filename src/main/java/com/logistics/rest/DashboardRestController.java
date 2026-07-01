package com.logistics.rest;

import com.logistics.dto.DashboardStatsDto;
import com.logistics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

    private final DashboardService service;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @GetMapping("/revenue")
    public ResponseEntity<List<Map<String, Object>>> getRevenue() {
        return ResponseEntity.ok(service.getRevenueChartData());
    }

    @GetMapping("/shipments-chart")
    public ResponseEntity<List<Map<String, Object>>> getShipmentChart() {
        return ResponseEntity.ok(service.getShipmentChartData());
    }

    @GetMapping("/warehouse-util")
    public ResponseEntity<List<Map<String, Object>>> getWarehouseUtil() {
        return ResponseEntity.ok(service.getWarehouseUtilization());
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<List<Map<String, Object>>> getRecentOrders() {
        return ResponseEntity.ok(service.getRecentOrders());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStock() {
        return ResponseEntity.ok(service.getLowStockProducts());
    }
}

package com.logistics.service;

import com.logistics.dto.DashboardStatsDto;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    DashboardStatsDto getStats();
    List<Map<String, Object>> getRevenueChartData();
    List<Map<String, Object>> getShipmentChartData();
    List<Map<String, Object>> getWarehouseUtilization();
    List<Map<String, Object>> getTopProducts();
    List<Map<String, Object>> getRecentOrders();
    List<Map<String, Object>> getRecentShipments();
    List<Map<String, Object>> getLowStockProducts();
}

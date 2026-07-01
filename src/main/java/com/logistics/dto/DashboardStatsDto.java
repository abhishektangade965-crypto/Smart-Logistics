package com.logistics.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsDto {
    private long totalOrders;
    private long pendingOrders;
    private long totalShipments;
    private long activeShipments;
    private long totalWarehouses;
    private BigDecimal inventoryValue;
    private BigDecimal totalRevenue;
    private long activeVehicles;
    private long delayedDeliveries;
    private long lowStockItems;
    private long totalCustomers;
    private long totalProducts;
    private long totalSuppliers;
}

package com.logistics.serviceimpl;

import com.logistics.dto.DashboardStatsDto;
import com.logistics.entity.CustomerOrder;
import com.logistics.repository.*;
import com.logistics.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerOrderRepository orderRepo;
    private final ShipmentRepository shipmentRepo;
    private final WarehouseRepository warehouseRepo;
    private final InventoryRepository inventoryRepo;
    private final VehicleRepository vehicleRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;

    @Override
    @Cacheable(value = "dashboardStats", key = "'summary'")
    public DashboardStatsDto getStats() {
        BigDecimal invValue = inventoryRepo.calculateTotalInventoryValue();
        BigDecimal revenue = orderRepo.calculateTotalRevenue();
        return DashboardStatsDto.builder()
                .totalOrders(orderRepo.count())
                .pendingOrders(orderRepo.countPendingOrders())
                .totalShipments(shipmentRepo.count())
                .activeShipments(shipmentRepo.countActiveShipments())
                .totalWarehouses(warehouseRepo.countActiveWarehouses())
                .inventoryValue(invValue != null ? invValue : BigDecimal.ZERO)
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .activeVehicles(vehicleRepo.countOnRouteVehicles())
                .delayedDeliveries(shipmentRepo.countDelayed(LocalDate.now()))
                .lowStockItems(inventoryRepo.countLowStockItems())
                .totalCustomers(customerRepo.count())
                .totalProducts(productRepo.count())
                .totalSuppliers(supplierRepo.count())
                .build();
    }

    @Override
    public List<Map<String, Object>> getRevenueChartData() {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Random rand = new Random(42);
        for (String m : months) {
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("month", m);
            point.put("revenue", 50000 + rand.nextInt(150000));
            point.put("cost", 30000 + rand.nextInt(80000));
            result.add(point);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getShipmentChartData() {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Random rand = new Random(99);
        for (String m : months) {
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("month", m);
            point.put("shipped", 100 + rand.nextInt(400));
            point.put("delivered", 80 + rand.nextInt(350));
            result.add(point);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getWarehouseUtilization() {
        List<Map<String, Object>> result = new ArrayList<>();
        warehouseRepo.findAllActive().forEach(w -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", w.getName());
            item.put("utilization", w.getUtilizationPercent());
            item.put("capacity", w.getTotalCapacity());
            result.add(item);
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getTopProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        productRepo.findByStatus(com.logistics.entity.Product.Status.ACTIVE).stream().limit(5).forEach(p -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", p.getName());
            item.put("sku", p.getSku());
            item.put("price", p.getUnitPrice());
            result.add(item);
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getRecentOrders() {
        List<Map<String, Object>> result = new ArrayList<>();
        orderRepo.findTop10ByOrderByCreatedAtDesc().forEach(o -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("orderNumber", o.getOrderNumber());
            item.put("customer", o.getCustomer() != null ? o.getCustomer().getName() : "N/A");
            item.put("total", o.getTotalAmount());
            item.put("status", o.getStatus().name());
            item.put("createdAt", o.getCreatedAt());
            result.add(item);
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getRecentShipments() {
        List<Map<String, Object>> result = new ArrayList<>();
        shipmentRepo.findTop10ByOrderByCreatedAtDesc().forEach(s -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("trackingNumber", s.getTrackingNumber());
            item.put("destination", s.getDestinationAddress());
            item.put("status", s.getStatus().name());
            item.put("estimatedDelivery", s.getEstimatedDelivery());
            result.add(item);
        });
        return result;
    }

    @Override
    public List<Map<String, Object>> getLowStockProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        inventoryRepo.findLowStock().stream().limit(10).forEach(inv -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("product", inv.getProduct().getName());
            item.put("sku", inv.getProduct().getSku());
            item.put("warehouse", inv.getWarehouse().getName());
            item.put("quantity", inv.getQuantityOnHand());
            item.put("reorderPoint", inv.getProduct().getReorderPoint());
            result.add(item);
        });
        return result;
    }
}

package com.logistics.controller;

import com.logistics.service.InventoryService;
import com.logistics.service.ProductService;
import com.logistics.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inventoryList", inventoryService.findAll());
        model.addAttribute("lowStockList", inventoryService.findLowStock());
        model.addAttribute("stats", inventoryService.getInventoryStats());
        model.addAttribute("pageTitle", "Inventory Management");
        model.addAttribute("activePage", "inventory");
        return "inventory/inventory";
    }

    @GetMapping("/movements")
    public String movements(Model model) {
        model.addAttribute("movements", inventoryService.findAllMovements());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());
        model.addAttribute("pageTitle", "Stock Movements");
        model.addAttribute("activePage", "inventory");
        return "inventory/movements";
    }
}

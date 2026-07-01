package com.logistics.controller;

import com.logistics.service.DashboardService;
import com.logistics.service.OrderService;
import com.logistics.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;

    @GetMapping
    public String reports(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("revenueData", dashboardService.getRevenueChartData());
        model.addAttribute("shipmentData", dashboardService.getShipmentChartData());
        model.addAttribute("warehouseUtil", dashboardService.getWarehouseUtilization());
        model.addAttribute("pageTitle", "Reports & Analytics");
        model.addAttribute("activePage", "reports");
        return "reports/reports";
    }
}

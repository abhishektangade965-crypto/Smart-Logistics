package com.logistics.controller;

import com.logistics.service.DashboardService;
import com.logistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("recentOrders", dashboardService.getRecentOrders());
        model.addAttribute("recentShipments", dashboardService.getRecentShipments());
        model.addAttribute("lowStockProducts", dashboardService.getLowStockProducts());
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");
        return "dashboard/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("user", userService.findByUsername(principal.getName()));
        }
        model.addAttribute("pageTitle", "My Profile");
        model.addAttribute("activePage", "profile");
        return "auth/profile";
    }
}

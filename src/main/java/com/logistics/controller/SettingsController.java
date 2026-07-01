package com.logistics.controller;

import com.logistics.service.CategoryService;
import com.logistics.service.RouteService;
import com.logistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final RouteService routeService;

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("pageTitle", "Settings");
        model.addAttribute("activePage", "settings");
        return "settings/settings";
    }
}

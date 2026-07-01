package com.logistics.controller;

import com.logistics.dto.WarehouseDto;
import com.logistics.entity.Warehouse;
import com.logistics.service.InventoryService;
import com.logistics.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;
    private final InventoryService inventoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("warehouses", service.findAll());
        model.addAttribute("pageTitle", "Warehouses");
        model.addAttribute("activePage", "warehouses");
        return "warehouses/warehouses";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("warehouse", new WarehouseDto());
        model.addAttribute("statuses", Arrays.asList(Warehouse.Status.values()));
        model.addAttribute("pageTitle", "New Warehouse");
        model.addAttribute("activePage", "warehouses");
        return "warehouses/warehouse-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("warehouse", service.findById(id));
        model.addAttribute("statuses", Arrays.asList(Warehouse.Status.values()));
        model.addAttribute("pageTitle", "Edit Warehouse");
        model.addAttribute("activePage", "warehouses");
        return "warehouses/warehouse-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("warehouse") WarehouseDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Arrays.asList(Warehouse.Status.values()));
            return "warehouses/warehouse-form";
        }
        if (dto.getId() == null) service.save(dto);
        else service.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Warehouse saved successfully.");
        return "redirect:/warehouses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Warehouse deleted.");
        return "redirect:/warehouses";
    }
}

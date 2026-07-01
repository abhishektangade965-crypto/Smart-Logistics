package com.logistics.controller;

import com.logistics.dto.SupplierDto;
import com.logistics.entity.Supplier;
import com.logistics.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("suppliers", service.findAll());
        model.addAttribute("pageTitle", "Suppliers");
        model.addAttribute("activePage", "suppliers");
        return "suppliers/suppliers";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("supplier", new SupplierDto());
        model.addAttribute("statuses", Arrays.asList(Supplier.Status.values()));
        model.addAttribute("pageTitle", "New Supplier");
        model.addAttribute("activePage", "suppliers");
        return "suppliers/supplier-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", service.findById(id));
        model.addAttribute("statuses", Arrays.asList(Supplier.Status.values()));
        model.addAttribute("pageTitle", "Edit Supplier");
        model.addAttribute("activePage", "suppliers");
        return "suppliers/supplier-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("supplier") SupplierDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", Arrays.asList(Supplier.Status.values()));
            return "suppliers/supplier-form";
        }
        if (dto.getId() == null) service.save(dto);
        else service.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Supplier saved successfully.");
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Supplier deleted.");
        return "redirect:/suppliers";
    }

    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", service.findById(id));
        model.addAttribute("pageTitle", "Supplier Details");
        model.addAttribute("activePage", "suppliers");
        return "suppliers/supplier-view";
    }
}

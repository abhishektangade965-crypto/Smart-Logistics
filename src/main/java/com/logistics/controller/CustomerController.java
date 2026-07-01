package com.logistics.controller;

import com.logistics.dto.CustomerDto;
import com.logistics.entity.Customer;
import com.logistics.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", service.findAll());
        model.addAttribute("pageTitle", "Customers");
        model.addAttribute("activePage", "customers");
        return "customers/customers";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customer", new CustomerDto());
        model.addAttribute("customerTypes", Arrays.asList(Customer.CustomerType.values()));
        model.addAttribute("statuses", Arrays.asList(Customer.Status.values()));
        model.addAttribute("pageTitle", "New Customer");
        model.addAttribute("activePage", "customers");
        return "customers/customer-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", service.findById(id));
        model.addAttribute("customerTypes", Arrays.asList(Customer.CustomerType.values()));
        model.addAttribute("statuses", Arrays.asList(Customer.Status.values()));
        model.addAttribute("pageTitle", "Edit Customer");
        model.addAttribute("activePage", "customers");
        return "customers/customer-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("customer") CustomerDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("customerTypes", Arrays.asList(Customer.CustomerType.values()));
            model.addAttribute("statuses", Arrays.asList(Customer.Status.values()));
            return "customers/customer-form";
        }
        if (dto.getId() == null) service.save(dto);
        else service.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Customer saved successfully.");
        return "redirect:/customers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Customer deleted.");
        return "redirect:/customers";
    }
}

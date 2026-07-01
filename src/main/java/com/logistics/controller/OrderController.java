package com.logistics.controller;

import com.logistics.dto.OrderDto;
import com.logistics.entity.CustomerOrder;
import com.logistics.service.CustomerService;
import com.logistics.service.OrderService;
import com.logistics.service.ProductService;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("activePage", "orders");
        return "orders/orders";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("order", new OrderDto());
        populateModel(model);
        model.addAttribute("pageTitle", "New Order");
        return "orders/order-form";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("statuses", Arrays.asList(CustomerOrder.OrderStatus.values()));
        model.addAttribute("pageTitle", "Order Details");
        model.addAttribute("activePage", "orders");
        return "orders/order-view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        populateModel(model);
        model.addAttribute("pageTitle", "Edit Order");
        return "orders/order-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("order") OrderDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) { populateModel(model); return "orders/order-form"; }
        if (dto.getId() == null) orderService.save(dto);
        else orderService.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Order saved successfully.");
        return "redirect:/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam CustomerOrder.OrderStatus status,
                               RedirectAttributes ra) {
        orderService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Order status updated.");
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        orderService.delete(id);
        ra.addFlashAttribute("success", "Order deleted.");
        return "redirect:/orders";
    }

    private void populateModel(Model model) {
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("orderStatuses", Arrays.asList(CustomerOrder.OrderStatus.values()));
        model.addAttribute("paymentStatuses", Arrays.asList(CustomerOrder.PaymentStatus.values()));
        model.addAttribute("activePage", "orders");
    }
}

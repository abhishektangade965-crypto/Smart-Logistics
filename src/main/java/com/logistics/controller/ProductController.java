package com.logistics.controller;

import com.logistics.dto.ProductDto;
import com.logistics.entity.Product;
import com.logistics.service.CategoryService;
import com.logistics.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("activePage", "products");
        return "products/products";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductDto());
        populateFormModel(model);
        model.addAttribute("pageTitle", "New Product");
        return "products/product-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        populateFormModel(model);
        model.addAttribute("pageTitle", "Edit Product");
        return "products/product-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("product") ProductDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) { populateFormModel(model); return "products/product-form"; }
        if (dto.getId() == null) productService.save(dto);
        else productService.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Product saved successfully.");
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        productService.delete(id);
        ra.addFlashAttribute("success", "Product deleted.");
        return "redirect:/products";
    }

    private void populateFormModel(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("statuses", Arrays.asList(Product.Status.values()));
        model.addAttribute("activePage", "products");
    }
}

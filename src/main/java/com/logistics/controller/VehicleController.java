package com.logistics.controller;

import com.logistics.dto.VehicleDto;
import com.logistics.entity.Vehicle;
import com.logistics.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vehicles", service.findAll());
        model.addAttribute("pageTitle", "Fleet Management");
        model.addAttribute("activePage", "vehicles");
        return "vehicles/vehicles";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("vehicle", new VehicleDto());
        model.addAttribute("vehicleTypes", Arrays.asList(Vehicle.VehicleType.values()));
        model.addAttribute("vehicleStatuses", Arrays.asList(Vehicle.VehicleStatus.values()));
        model.addAttribute("pageTitle", "Add Vehicle");
        model.addAttribute("activePage", "vehicles");
        return "vehicles/vehicle-form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("vehicle", service.findById(id));
        model.addAttribute("vehicleTypes", Arrays.asList(Vehicle.VehicleType.values()));
        model.addAttribute("vehicleStatuses", Arrays.asList(Vehicle.VehicleStatus.values()));
        model.addAttribute("pageTitle", "Edit Vehicle");
        model.addAttribute("activePage", "vehicles");
        return "vehicles/vehicle-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("vehicle") VehicleDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("vehicleTypes", Arrays.asList(Vehicle.VehicleType.values()));
            model.addAttribute("vehicleStatuses", Arrays.asList(Vehicle.VehicleStatus.values()));
            return "vehicles/vehicle-form";
        }
        if (dto.getId() == null) service.save(dto);
        else service.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Vehicle saved.");
        return "redirect:/vehicles";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Vehicle deleted.");
        return "redirect:/vehicles";
    }
}

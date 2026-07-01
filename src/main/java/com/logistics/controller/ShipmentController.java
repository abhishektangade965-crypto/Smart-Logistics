package com.logistics.controller;

import com.logistics.dto.ShipmentDto;
import com.logistics.entity.DeliveryTracking;
import com.logistics.entity.Shipment;
import com.logistics.service.OrderService;
import com.logistics.service.RouteService;
import com.logistics.service.ShipmentService;
import com.logistics.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final OrderService orderService;
    private final VehicleService vehicleService;
    private final RouteService routeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("shipments", shipmentService.findAll());
        model.addAttribute("pageTitle", "Shipments");
        model.addAttribute("activePage", "shipments");
        return "shipments/shipments";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("shipment", new ShipmentDto());
        populateModel(model);
        model.addAttribute("pageTitle", "New Shipment");
        return "shipments/shipment-form";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("shipment", shipmentService.findById(id));
        List<DeliveryTracking> events = shipmentService.getTrackingEvents(id);
        model.addAttribute("trackingEvents", events);
        model.addAttribute("trackingPoints", toPoints(events));
        model.addAttribute("statuses", Arrays.asList(Shipment.ShipmentStatus.values()));
        model.addAttribute("pageTitle", "Shipment Details");
        model.addAttribute("activePage", "shipments");
        return "shipments/shipment-view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("shipment", shipmentService.findById(id));
        populateModel(model);
        model.addAttribute("pageTitle", "Edit Shipment");
        return "shipments/shipment-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("shipment") ShipmentDto dto,
                       BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) { populateModel(model); return "shipments/shipment-form"; }
        if (dto.getId() == null) shipmentService.save(dto);
        else shipmentService.update(dto.getId(), dto);
        ra.addFlashAttribute("success", "Shipment saved.");
        return "redirect:/shipments";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        shipmentService.delete(id);
        ra.addFlashAttribute("success", "Shipment deleted.");
        return "redirect:/shipments";
    }

    @GetMapping("/track/{trackingNumber}")
    public String track(@PathVariable String trackingNumber, Model model) {
        ShipmentDto shipment = shipmentService.findByTrackingNumber(trackingNumber);
        model.addAttribute("shipment", shipment);
        List<DeliveryTracking> events = shipmentService.getTrackingEvents(shipment.getId());
        model.addAttribute("trackingEvents", events);
        model.addAttribute("trackingPoints", toPoints(events));
        model.addAttribute("statuses", Arrays.asList(Shipment.ShipmentStatus.values()));
        model.addAttribute("pageTitle", "Track Shipment");
        return "shipments/tracking";
    }

    @GetMapping("/track")
    public String trackSearch(Model model) {
        model.addAttribute("shipments", shipmentService.findAll());
        model.addAttribute("pageTitle", "Track Shipment");
        model.addAttribute("activePage", "track");
        return "shipments/track-search";
    }

    /**
     * Flattens DeliveryTracking entities into plain maps (no back-reference to Shipment)
     * so they can be safely serialized to JSON for the Leaflet map widgets.
     */
    private List<Map<String, Object>> toPoints(List<DeliveryTracking> events) {
        return events.stream().map(e -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("status", e.getStatus() != null ? e.getStatus().name() : null);
            m.put("location", e.getLocation());
            m.put("description", e.getDescription());
            m.put("latitude", e.getLatitude());
            m.put("longitude", e.getLongitude());
            m.put("timestamp", e.getTimestamp() != null ? e.getTimestamp().toString() : null);
            return m;
        }).collect(Collectors.toList());
    }

    private void populateModel(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("vehicles", vehicleService.findAll());
        model.addAttribute("routes", routeService.findAll());
        model.addAttribute("shipmentStatuses", Arrays.asList(Shipment.ShipmentStatus.values()));
        model.addAttribute("activePage", "shipments");
    }
}

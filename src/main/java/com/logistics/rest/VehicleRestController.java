package com.logistics.rest;

import com.logistics.dto.VehicleDto;
import com.logistics.entity.Vehicle;
import com.logistics.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleRestController {

    private final VehicleService service;

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getById(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<VehicleDto> create(@Valid @RequestBody VehicleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@PathVariable Long id, @Valid @RequestBody VehicleDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleDto> updateStatus(@PathVariable Long id,
                                                    @RequestParam Vehicle.VehicleStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}

package com.logistics.rest;

import com.logistics.dto.OrderDto;
import com.logistics.entity.CustomerOrder;
import com.logistics.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService service;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id,
                                                  @RequestParam CustomerOrder.OrderStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> byCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.findByCustomer(customerId));
    }
}

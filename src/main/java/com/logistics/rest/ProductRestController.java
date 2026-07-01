package com.logistics.rest;

import com.logistics.dto.ProductDto;
import com.logistics.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() { return ResponseEntity.ok(service.findAll()); }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductDto>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        
        String[] sortParts = sort.split(",");
        Sort sortOrder = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") 
                ? Sort.by(sortParts[0]).ascending()
                : Sort.by(sortParts[0]).descending();
                
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return ResponseEntity.ok(service.findAllPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) { return ResponseEntity.ok(service.findById(id)); }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDto> getBySku(@PathVariable String sku) { return ResponseEntity.ok(service.findBySku(sku)); }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> search(@RequestParam String q) {
        return ResponseEntity.ok(service.search(q));
    }
}

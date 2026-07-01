package com.logistics.serviceimpl;

import com.logistics.dto.WarehouseDto;
import com.logistics.entity.Warehouse;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.WarehouseRepository;
import com.logistics.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repo;

    @Override @Transactional(readOnly = true)
    public List<WarehouseDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public WarehouseDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Warehouse", "id", id)));
    }

    @Override
    public WarehouseDto save(WarehouseDto dto) {
        Warehouse w = new Warehouse();
        copyFields(dto, w);
        return toDto(repo.save(w));
    }

    @Override
    public WarehouseDto update(Long id, WarehouseDto dto) {
        Warehouse w = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Warehouse", "id", id));
        copyFields(dto, w);
        return toDto(repo.save(w));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Warehouse", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<WarehouseDto> findActive() {
        return repo.findAllActive().stream().map(this::toDto).collect(Collectors.toList());
    }

    private void copyFields(WarehouseDto dto, Warehouse w) {
        w.setName(dto.getName()); w.setCode(dto.getCode()); w.setAddress(dto.getAddress());
        w.setCity(dto.getCity()); w.setState(dto.getState()); w.setCountry(dto.getCountry());
        w.setZipCode(dto.getZipCode()); w.setManagerName(dto.getManagerName());
        w.setPhone(dto.getPhone()); w.setEmail(dto.getEmail());
        
        // FIXED: Safe conversion from Double (DTO) to BigDecimal (Entity)
        w.setTotalCapacity(dto.getTotalCapacity() != null ? BigDecimal.valueOf(dto.getTotalCapacity()) : null);
        w.setUsedCapacity(dto.getUsedCapacity() != null ? BigDecimal.valueOf(dto.getUsedCapacity()) : BigDecimal.ZERO);
        
        w.setCapacityUnit(dto.getCapacityUnit());
        w.setStatus(dto.getStatus() != null ? dto.getStatus() : Warehouse.Status.ACTIVE);
        w.setNotes(dto.getNotes());
    }

    private WarehouseDto toDto(Warehouse w) {
        return WarehouseDto.builder()
                .id(w.getId()).name(w.getName()).code(w.getCode()).address(w.getAddress())
                .city(w.getCity()).state(w.getState()).country(w.getCountry())
                .zipCode(w.getZipCode()).managerName(w.getManagerName())
                .phone(w.getPhone()).email(w.getEmail())
                
                // FIXED: Safe conversion from BigDecimal (Entity) to Double (DTO)
                .totalCapacity(w.getTotalCapacity() != null ? w.getTotalCapacity().doubleValue() : null)
                .usedCapacity(w.getUsedCapacity() != null ? w.getUsedCapacity().doubleValue() : 0.0)
                
                .capacityUnit(w.getCapacityUnit())
                .status(w.getStatus()).notes(w.getNotes())
                .utilizationPercent(w.getUtilizationPercent()).build();
    }
}
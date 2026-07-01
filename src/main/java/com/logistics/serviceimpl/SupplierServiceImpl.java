package com.logistics.serviceimpl;

import com.logistics.dto.SupplierDto;
import com.logistics.entity.Supplier;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.SupplierRepository;
import com.logistics.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repo;

    @Override @Transactional(readOnly = true)
    public List<SupplierDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public SupplierDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier", "id", id)));
    }

    @Override
    public SupplierDto save(SupplierDto dto) {
        Supplier s = new Supplier();
        copyFields(dto, s);
        return toDto(repo.save(s));
    }

    @Override
    public SupplierDto update(Long id, SupplierDto dto) {
        Supplier s = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Supplier", "id", id));
        copyFields(dto, s);
        return toDto(repo.save(s));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Supplier", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<SupplierDto> search(String query) {
        return repo.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<SupplierDto> findByStatus(String status) {
        return repo.findByStatus(Supplier.Status.valueOf(status))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private void copyFields(SupplierDto dto, Supplier s) {
        s.setName(dto.getName());
        s.setCode(dto.getCode());
        s.setEmail(dto.getEmail());
        s.setPhone(dto.getPhone());
        s.setAddress(dto.getAddress());
        s.setCity(dto.getCity());
        s.setCountry(dto.getCountry());
        s.setContactPerson(dto.getContactPerson());
        s.setWebsite(dto.getWebsite());
        s.setStatus(dto.getStatus() != null ? dto.getStatus() : Supplier.Status.ACTIVE);
        s.setNotes(dto.getNotes());
    }

    private SupplierDto toDto(Supplier s) {
        return SupplierDto.builder()
                .id(s.getId()).name(s.getName()).code(s.getCode()).email(s.getEmail())
                .phone(s.getPhone()).address(s.getAddress()).city(s.getCity())
                .country(s.getCountry()).contactPerson(s.getContactPerson())
                .website(s.getWebsite()).status(s.getStatus()).notes(s.getNotes()).build();
    }
}

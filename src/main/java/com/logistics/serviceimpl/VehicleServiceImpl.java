package com.logistics.serviceimpl;

import com.logistics.dto.VehicleDto;
import com.logistics.entity.Vehicle;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.VehicleRepository;
import com.logistics.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repo;

    @Override @Transactional(readOnly = true)
    public List<VehicleDto> findAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public VehicleDto findById(Long id) {
        return toDto(repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle", "id", id)));
    }

    @Override
    public VehicleDto save(VehicleDto dto) {
        Vehicle v = new Vehicle();
        copyFields(dto, v);
        return toDto(repo.save(v));
    }

    @Override
    public VehicleDto update(Long id, VehicleDto dto) {
        Vehicle v = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle", "id", id));
        copyFields(dto, v);
        return toDto(repo.save(v));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Vehicle", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<VehicleDto> findByStatus(Vehicle.VehicleStatus status) {
        return repo.findByStatus(status).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public VehicleDto updateStatus(Long id, Vehicle.VehicleStatus status) {
        Vehicle v = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle", "id", id));
        v.setStatus(status);
        return toDto(repo.save(v));
    }

    private void copyFields(VehicleDto dto, Vehicle v) {
        v.setLicensePlate(dto.getLicensePlate()); v.setMake(dto.getMake()); v.setModel(dto.getModel());
        v.setYear(dto.getYear()); v.setType(dto.getType()); v.setMaxLoadCapacity(dto.getMaxLoadCapacity());
        v.setCapacityUnit(dto.getCapacityUnit()); v.setDriverName(dto.getDriverName());
        v.setDriverPhone(dto.getDriverPhone()); v.setDriverLicense(dto.getDriverLicense());
        v.setStatus(dto.getStatus() != null ? dto.getStatus() : Vehicle.VehicleStatus.AVAILABLE);
        v.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        v.setNextMaintenanceDate(dto.getNextMaintenanceDate());
        v.setInsuranceExpiry(dto.getInsuranceExpiry()); v.setTotalMileage(dto.getTotalMileage());
        v.setNotes(dto.getNotes());
    }

    private VehicleDto toDto(Vehicle v) {
        return VehicleDto.builder()
                .id(v.getId()).licensePlate(v.getLicensePlate()).make(v.getMake()).model(v.getModel())
                .year(v.getYear()).type(v.getType()).maxLoadCapacity(v.getMaxLoadCapacity())
                .capacityUnit(v.getCapacityUnit()).driverName(v.getDriverName())
                .driverPhone(v.getDriverPhone()).driverLicense(v.getDriverLicense())
                .status(v.getStatus()).lastMaintenanceDate(v.getLastMaintenanceDate())
                .nextMaintenanceDate(v.getNextMaintenanceDate())
                .insuranceExpiry(v.getInsuranceExpiry()).totalMileage(v.getTotalMileage())
                .notes(v.getNotes()).build();
    }
}

package com.logistics.serviceimpl;

import com.logistics.entity.Route;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.RouteRepository;
import com.logistics.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {

    private final RouteRepository repo;

    @Override @Transactional(readOnly = true)
    public List<Route> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Route findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Route", "id", id));
    }

    @Override
    public Route save(Route route) { return repo.save(route); }

    @Override
    public Route update(Long id, Route route) {
        Route existing = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Route", "id", id));
        existing.setName(route.getName()); existing.setCode(route.getCode());
        existing.setOrigin(route.getOrigin()); existing.setDestination(route.getDestination());
        existing.setDistanceKm(route.getDistanceKm()); existing.setEstimatedHours(route.getEstimatedHours());
        existing.setStatus(route.getStatus());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Route", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<Route> findActive() { return repo.findByStatus(Route.Status.ACTIVE); }
}

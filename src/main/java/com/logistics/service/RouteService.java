package com.logistics.service;

import com.logistics.entity.Route;

import java.util.List;

public interface RouteService {
    List<Route> findAll();
    Route findById(Long id);
    Route save(Route route);
    Route update(Long id, Route route);
    void delete(Long id);
    List<Route> findActive();
}

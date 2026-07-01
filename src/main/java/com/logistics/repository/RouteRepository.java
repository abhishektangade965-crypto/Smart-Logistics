package com.logistics.repository;

import com.logistics.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByCode(String code);
    List<Route> findByStatus(Route.Status status);
    boolean existsByCode(String code);
}

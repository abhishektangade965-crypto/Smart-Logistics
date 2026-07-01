package com.logistics.repository;

import com.logistics.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByCode(String code);
    List<Warehouse> findByStatus(Warehouse.Status status);
    boolean existsByCode(String code);

    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.status = 'ACTIVE'")
    long countActiveWarehouses();

    @Query("SELECT w FROM Warehouse w WHERE w.status = 'ACTIVE' ORDER BY w.name")
    List<Warehouse> findAllActive();
}

package com.logistics.repository;

import com.logistics.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByCode(String code);
    List<Supplier> findByStatus(Supplier.Status status);
    List<Supplier> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);
    boolean existsByCode(String code);

    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.status = 'ACTIVE'")
    long countActiveSuppliers();
}

package com.logistics.repository;

import com.logistics.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductId(Long productId);
    List<StockMovement> findByMovementType(StockMovement.MovementType type);
    List<StockMovement> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<StockMovement> findTop20ByOrderByCreatedAtDesc();
    Optional<StockMovement> findByReferenceNumber(String referenceNumber);

    @Query("SELECT COUNT(m) FROM StockMovement m WHERE m.createdAt >= :since")
    long countSince(LocalDateTime since);
}

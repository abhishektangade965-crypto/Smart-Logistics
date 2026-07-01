package com.logistics.repository;

import com.logistics.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    Optional<CustomerOrder> findByOrderNumber(String orderNumber);
    List<CustomerOrder> findByStatus(CustomerOrder.OrderStatus status);
    List<CustomerOrder> findByCustomerId(Long customerId);
    List<CustomerOrder> findTop10ByOrderByCreatedAtDesc();
    List<CustomerOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status = 'PENDING'")
    long countPendingOrders();

    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE o.status NOT IN ('CANCELLED', 'DELIVERED')")
    long countActiveOrders();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM CustomerOrder o WHERE o.paymentStatus = 'PAID'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM CustomerOrder o WHERE o.createdAt >= :since AND o.paymentStatus = 'PAID'")
    BigDecimal calculateRevenueSince(LocalDateTime since);

    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE o.createdAt >= :since")
    long countOrdersSince(LocalDateTime since);
}

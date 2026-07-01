package com.logistics.repository;

import com.logistics.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCode(String code);
    List<Customer> findByStatus(Customer.Status status);
    boolean existsByCode(String code);
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:q% OR c.code LIKE %:q% OR c.email LIKE %:q%")
    List<Customer> search(String q);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = 'ACTIVE'")
    long countActiveCustomers();
}

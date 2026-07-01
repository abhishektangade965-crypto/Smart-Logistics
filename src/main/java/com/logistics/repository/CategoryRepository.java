package com.logistics.repository;

import com.logistics.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();
    List<Category> findByParentId(Long parentId);
    List<Category> findByActiveTrue();
    List<Category> findByNameContainingIgnoreCase(String name);
    java.util.Optional<Category> findByCode(String code);
    boolean existsByCode(String code);
}

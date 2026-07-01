package com.logistics.service;

import com.logistics.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
    List<Category> findRootCategories();
}

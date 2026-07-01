package com.logistics.serviceimpl;

import com.logistics.entity.Category;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CategoryRepository;
import com.logistics.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override @Transactional(readOnly = true)
    public List<Category> findAll() { return repo.findAll(); }

    @Override @Transactional(readOnly = true)
    public Category findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public Category save(Category category) { return repo.save(category); }

    @Override
    public Category update(Long id, Category category) {
        Category existing = repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", id));
        existing.setName(category.getName());
        existing.setCode(category.getCode());
        existing.setDescription(category.getDescription());
        existing.setActive(category.isActive());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Category", "id", id);
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<Category> findRootCategories() { return repo.findByParentIsNull(); }
}

package com.ezwell.backend.domain.category;

import com.ezwell.backend.domain.category.dto.CategoryCreateRequest;
import com.ezwell.backend.domain.category.dto.CategoryResponse;
import com.ezwell.backend.domain.category.dto.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = new Category(
                request.name(),
                request.status(),
                request.displayOrder()
        );

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        category.update(
                request.name(),
                request.status(),
                request.displayOrder()
        );

        return CategoryResponse.from(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        categoryRepository.delete(category);
    }
}
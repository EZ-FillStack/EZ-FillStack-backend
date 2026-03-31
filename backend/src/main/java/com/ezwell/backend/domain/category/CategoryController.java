package com.ezwell.backend.domain.category;

import com.ezwell.backend.domain.category.dto.CategoryCreateRequest;
import com.ezwell.backend.domain.category.dto.CategoryResponse;
import com.ezwell.backend.domain.category.dto.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public CategoryResponse create(@RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @RequestBody CategoryUpdateRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
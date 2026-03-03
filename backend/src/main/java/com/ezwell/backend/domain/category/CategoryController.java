package com.ezwell.backend.domain.category;

import com.ezwell.backend.domain.category.dto.CategoryCreateRequest;
import com.ezwell.backend.domain.category.dto.CategoryResponse;
import com.ezwell.backend.domain.category.dto.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 카테고리 관리 API
 *
 * - /admin/** 경로이므로 ADMIN 권한 필요
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 전체 조회
     */
    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    /**
     * 카테고리 생성
     */
    @PostMapping
    public CategoryResponse create(@RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    /**
     * 카테고리 수정
     */
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @RequestBody CategoryUpdateRequest request) {
        return categoryService.update(id, request);
    }

    /**
     * 카테고리 삭제
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
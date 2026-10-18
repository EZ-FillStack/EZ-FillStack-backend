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
public class AdminCategoryController {

    private final CategoryService categoryService;

    //관리자용 전체 목록 조회
    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }

    //관리자용 관리자용 카테고리 생성
    @PostMapping
    public CategoryResponse create(@RequestBody CategoryCreateRequest request) {
        return categoryService.create(request);
    }

    //관리자용 카테고리 수정
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @RequestBody CategoryUpdateRequest request) {
        return categoryService.update(id, request);
    }

    //관리자용 카테고리 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
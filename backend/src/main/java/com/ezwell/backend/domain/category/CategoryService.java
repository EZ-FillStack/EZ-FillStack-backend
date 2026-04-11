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

    //노출 순서대로 정렬된 데이터를 가져옴
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    //카테고리 단건 상세 조회
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        return CategoryResponse.from(category);
    }

    //카테고리 생성
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = new Category(
                request.name(),
                request.status(),
                request.displayOrder()
        );

        return CategoryResponse.from(categoryRepository.save(category));
    }

    //카테고리 업데이트
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

    //카테고리 삭제
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        categoryRepository.delete(category);
    }
}
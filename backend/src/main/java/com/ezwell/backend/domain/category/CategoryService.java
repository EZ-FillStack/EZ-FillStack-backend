package com.ezwell.backend.domain.category;

import com.ezwell.backend.domain.category.dto.CategoryCreateRequest;
import com.ezwell.backend.domain.category.dto.CategoryResponse;
import com.ezwell.backend.domain.category.dto.CategoryUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 카테고리 서비스
 *
 * - 관리자 전용 CRUD 처리
 * - 토이 프로젝트 기준 단순 로직
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 전체 카테고리 조회
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {

        // 엔티티 직접 생성
        Category category = new Category();

        try {
            // 리플렉션을 이용해 필드 세팅 (기존 구조 유지)
            java.lang.reflect.Field nameField = Category.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(category, request.name());

            java.lang.reflect.Field statusField = Category.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(category, request.status());

            java.lang.reflect.Field orderField = Category.class.getDeclaredField("displayOrder");
            orderField.setAccessible(true);
            orderField.set(category, request.displayOrder());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CategoryResponse.from(categoryRepository.save(category));
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse update(Long id, CategoryUpdateRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        try {
            if (request.name() != null) {
                java.lang.reflect.Field nameField = Category.class.getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(category, request.name());
            }
            if (request.status() != null) {
                java.lang.reflect.Field statusField = Category.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(category, request.status());
            }
            if (request.displayOrder() != null) {
                java.lang.reflect.Field orderField = Category.class.getDeclaredField("displayOrder");
                orderField.setAccessible(true);
                orderField.set(category, request.displayOrder());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CategoryResponse.from(category);
    }

    /**
     * 카테고리 삭제 (물리 삭제)
     */
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        categoryRepository.delete(category);
    }
}
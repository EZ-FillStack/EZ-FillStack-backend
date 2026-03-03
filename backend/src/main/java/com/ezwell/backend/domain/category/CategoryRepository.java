package com.ezwell.backend.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리 Repository
 * - 기본 CRUD 제공
 * - 토이 프로젝트이므로 복잡한 쿼리는 추가하지 않음
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
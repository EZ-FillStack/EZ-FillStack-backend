package com.ezwell.backend.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

//카테고리 조회, pk 타입 Long
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
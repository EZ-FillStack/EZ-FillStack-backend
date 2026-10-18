package com.ezwell.backend.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 노출 순서기준 오름차순으로 정렬하여 조회
    List<Category> findAllByOrderByDisplayOrderAsc();
}
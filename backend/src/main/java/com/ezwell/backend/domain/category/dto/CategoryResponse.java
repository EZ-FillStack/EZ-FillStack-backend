package com.ezwell.backend.domain.category.dto;

import com.ezwell.backend.domain.category.Category;

public record CategoryResponse(
        Long id,
        String name,
        String status,
        Integer displayOrder
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getStatus(),
                c.getDisplayOrder()
        );
    }
}
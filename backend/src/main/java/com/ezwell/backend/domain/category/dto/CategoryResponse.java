package com.ezwell.backend.domain.category.dto;

import com.ezwell.backend.domain.category.Category;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        String status,
        Integer displayOrder,
        LocalDateTime createdAt
) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(
                c.getId(),
                c.getName(),
                c.getStatus(),
                c.getDisplayOrder(),
                c.getCreatedAt()
        );
    }
}
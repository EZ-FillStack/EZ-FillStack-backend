package com.ezwell.backend.domain.category.dto;

public record CategoryUpdateRequest(
        String name,
        String status,
        Integer displayOrder
) {
}
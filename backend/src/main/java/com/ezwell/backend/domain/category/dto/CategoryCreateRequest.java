package com.ezwell.backend.domain.category.dto;

public record CategoryCreateRequest(
        String name,
        String status,
        Integer displayOrder
) {
}
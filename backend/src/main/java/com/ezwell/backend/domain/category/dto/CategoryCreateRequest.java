package com.ezwell.backend.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(

        @NotBlank
        String name,

        @NotBlank
        String status,

        @NotNull
        Integer displayOrder
) {}
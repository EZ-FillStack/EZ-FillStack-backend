package com.ezwell.backend.domain.review.dto;

public record ReviewUpdateRequest(
        int rating,
        String content
) {}
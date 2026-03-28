package com.ezwell.backend.domain.review.dto;

public record ReviewCreateRequest(
        Long eventId,
        int rating,
        String content
) {}
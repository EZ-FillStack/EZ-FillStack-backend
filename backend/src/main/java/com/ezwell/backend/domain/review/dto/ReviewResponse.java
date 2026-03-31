package com.ezwell.backend.domain.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long userId,
        Long eventId,
        String eventTitle,
        int rating,
        String content,
        int recommendCount,
        LocalDateTime createdAt
) {}
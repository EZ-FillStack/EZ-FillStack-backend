package com.ezwell.backend.domain.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long reviewId,
        Long userId,
        Long eventId,
        String eventTitle,
        String nickname,
        int rating,
        String content,
        int recommendCount,
        boolean isLiked,
        LocalDateTime createdAt
) {}
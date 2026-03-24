package com.ezwell.backend.domain.review.dto;

import com.ezwell.backend.domain.review.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long memberId,
        Long eventId,
        String nickname,
        String title,
        int rating,
        String content,
        long likeCount,
        LocalDateTime createdAt,
        boolean voted

){
}
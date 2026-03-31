package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import com.ezwell.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping
    public void createReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewCreateRequest request
    ) {
        reviewService.createReview(user.getUserId(), request);
    }

    // 리뷰 조회
    @GetMapping("/events/{eventId}")
    public List<ReviewResponse> getReviews(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return reviewService.getReviews(eventId, user.getUserId());
    }

    // 좋아요 토글
    @PostMapping("/{reviewId}/like")
    public void like(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        reviewService.toggleLike(user.getUserId(), reviewId);
    }
}
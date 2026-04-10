package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import com.ezwell.backend.domain.review.dto.ReviewUpdateRequest;
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

    //리뷰 작성
    @PostMapping
    public void createReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReviewCreateRequest request
    ) {
        reviewService.createReview(user.getUserId(), request);
    }

    //특정 이벤트 리뷰 조회
    @GetMapping("/events/{eventId}")
    public List<ReviewResponse> getReviews(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long currentUserId = (user != null) ? user.getUserId() : 0L;
        return reviewService.getReviews(eventId, currentUserId);
    }

    // 베스트 리뷰 전체 목록 조회
    @GetMapping("/best")
    public List<ReviewResponse> getBestReviews(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long currentUserId = (user != null) ? user.getUserId() : 0L;
        return reviewService.getBestReviews(currentUserId);
    }

    // 리뷰 수정
    @PatchMapping("/{reviewId}")
    public void updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        reviewService.updateReview(user.getUserId(), reviewId, request);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        reviewService.deleteReview(user.getUserId(), reviewId);
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
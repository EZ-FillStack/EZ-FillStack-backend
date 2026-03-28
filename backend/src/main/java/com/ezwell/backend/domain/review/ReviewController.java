package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping
    public void createReview(@RequestBody ReviewCreateRequest request) {

        Long userId = 1L; // JWT로 교체 예정

        reviewService.createReview(userId, request);
    }

    // 리뷰 조회
    @GetMapping("/events/{eventId}")
    public List<ReviewResponse> getReviews(@PathVariable Long eventId) {

        Long userId = 1L; // JWT로 변경 예정

        return reviewService.getReviews(eventId, userId);
    }

    // hasReview
    @GetMapping("/events/{eventId}/me")
    public boolean hasReview(@PathVariable Long eventId) {

        Long userId = 1L;

        return reviewService.hasReview(userId, eventId);
    }
}
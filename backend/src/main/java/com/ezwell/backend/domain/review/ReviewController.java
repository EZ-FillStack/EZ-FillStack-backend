package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import com.ezwell.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public void createReview(
            @RequestHeader("Authorization") String token,
            @RequestBody ReviewCreateRequest request
    ) {
        Long userId = extractUserId(token);
        reviewService.createReview(userId, request);
    }

    @GetMapping("/events/{eventId}")
    public List<ReviewResponse> getReviews(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId
    ) {
        Long userId = extractUserId(token);
        return reviewService.getReviews(eventId, userId);
    }

    @GetMapping("/events/{eventId}/me")
    public boolean hasReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Long eventId
    ) {
        Long userId = extractUserId(token);
        return reviewService.hasReview(userId, eventId);
    }

    private Long extractUserId(String token) {
        String pureToken = token.replace("Bearer ", "");

        return jwtTokenProvider.parse(pureToken)
                .getBody()
                .get("userId", Integer.class)
                .longValue();
    }
}
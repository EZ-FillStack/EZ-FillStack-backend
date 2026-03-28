package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 작성 (중복 방지)
    public void createReview(Long userId, ReviewCreateRequest request) {

        if (reviewRepository.existsByMemberIdAndEventId(userId, request.eventId())) {
            throw new IllegalStateException("ALREADY_REVIEWED");
        }

        Review review = new Review(
                userId,
                request.eventId(),
                request.rating(),
                request.content()
        );

        reviewRepository.save(review);
    }

    // 조회
    public List<ReviewResponse> getReviews(Long eventId, Long userId) {
        return reviewRepository.findReviewsWithLikeAndVoted(eventId, userId);
    }

    // hasReview
    public boolean hasReview(Long userId, Long eventId) {
        return reviewRepository.existsByMemberIdAndEventId(userId, eventId);
    }
}
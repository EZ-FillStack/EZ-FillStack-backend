package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final EventRepository eventRepository; // 🔥 추가

    // 리뷰 작성
    public void createReview(Long userId, ReviewCreateRequest request) {

        // event 객체 기준
        if (reviewRepository.existsByUserIdAndEvent_Id(userId, request.eventId())) {
            throw new IllegalStateException("ALREADY_REVIEWED");
        }

        // event 조회 추가
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

        // 생성자 변경
        Review review = new Review(
                userId,
                event,
                request.rating(),
                request.content()
        );

        reviewRepository.save(review);
    }

    // 리뷰 조회
    public List<ReviewResponse> getReviews(Long eventId, Long userId) {
        return reviewRepository.findReviews(eventId, userId);
    }

    // 좋아요 토글
    public void toggleLike(Long userId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("REVIEW_NOT_FOUND"));

        reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
                .ifPresentOrElse(like -> {
                    // 취소
                    reviewLikeRepository.delete(like);
                    review.decreaseRecommend();
                }, () -> {
                    // 생성
                    reviewLikeRepository.save(new ReviewLike(userId, reviewId));
                    review.increaseRecommend();
                });
    }
}
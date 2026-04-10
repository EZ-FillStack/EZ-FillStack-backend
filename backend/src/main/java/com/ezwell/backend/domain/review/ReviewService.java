package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import com.ezwell.backend.domain.review.dto.ReviewUpdateRequest; // (추가)
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
    private final EventRepository eventRepository;

    // 리뷰 작성
    public void createReview(Long userId, ReviewCreateRequest request) {
        if (reviewRepository.existsByUserIdAndEvent_Id(userId, request.eventId())) {
            throw new IllegalStateException("ALREADY_REVIEWED");
        }
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));
        Review review = new Review(userId, event, request.rating(), request.content());
        reviewRepository.save(review);
    }

    // 이벤트별 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long eventId, Long userId) {
        return reviewRepository.findReviews(eventId, userId);
    }

    // 베스트 리뷰 조회 로직
    @Transactional(readOnly = true)
    public List<ReviewResponse> getBestReviews(Long userId) {
        return reviewRepository.findBestReviews(userId);
    }

    // 리뷰 수정
    public void updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("REVIEW_NOT_FOUND"));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("NOT_AUTHORIZED_TO_UPDATE");
        }

        review.update(request.rating(), request.content());
    }

    // 리뷰 삭제
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("REVIEW_NOT_FOUND"));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("NOT_AUTHORIZED_TO_DELETE");
        }

        reviewRepository.delete(review);
    }

    // 좋아요 토글
    public void toggleLike(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("REVIEW_NOT_FOUND"));

        reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
                .ifPresentOrElse(like -> {
                    reviewLikeRepository.delete(like);
                    review.decreaseRecommend();
                }, () -> {
                    reviewLikeRepository.save(new ReviewLike(userId, reviewId));
                    review.increaseRecommend();
                });
    }
}
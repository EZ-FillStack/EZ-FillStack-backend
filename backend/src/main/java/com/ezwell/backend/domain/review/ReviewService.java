package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.review.dto.ReviewCreateRequest;
import com.ezwell.backend.domain.review.dto.ReviewResponse;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public void createReview(Long userId, ReviewCreateRequest request) {

        if (reviewRepository.existsByUser_IdAndEvent_Id(userId, request.eventId())) {
            throw new IllegalStateException("ALREADY_REVIEWED");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

        Review review = new Review(
                user,
                event,
                request.content(),
                request.rating()
        );

        reviewRepository.save(review);
    }

    public List<ReviewResponse> getReviews(Long eventId, Long userId) {
        return reviewRepository.findReviewsWithLikeAndVoted(eventId, userId);
    }

    public boolean hasReview(Long userId, Long eventId) {
        return reviewRepository.existsByUser_IdAndEvent_Id(userId, eventId);
    }
}
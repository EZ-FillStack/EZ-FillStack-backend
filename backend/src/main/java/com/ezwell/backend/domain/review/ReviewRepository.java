package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT new com.ezwell.backend.domain.review.dto.ReviewResponse(
        r.id,
        r.user.id,
        r.event.id,
        u.nickname,
        e.title,
        r.rating,
        r.content,
        COUNT(rl.id),
        CASE 
            WHEN COUNT(rl2.id) > 0 THEN true 
            ELSE false 
        END,
        r.createdAt
    )
    FROM Review r
    JOIN r.user u
    JOIN r.event e
    LEFT JOIN ReviewLike rl ON rl.review = r
    LEFT JOIN ReviewLike rl2 
        ON rl2.review = r AND rl2.user.id = :userId
    WHERE r.event.id = :eventId
    GROUP BY r.id, r.user.id, r.event.id, u.nickname, e.title, r.rating, r.content, r.createdAt
    """)
    List<ReviewResponse> findReviewsWithLikeAndVoted(Long eventId, Long userId);

    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);
}
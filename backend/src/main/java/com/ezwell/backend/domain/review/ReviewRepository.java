package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndEvent_Id(Long userId, Long eventId);

    @Query("""
    SELECT new com.ezwell.backend.domain.review.dto.ReviewResponse(
        r.id,
        r.userId,
        r.event.id,
        u.nickname,
        e.title,
        r.rating,
        r.content,
        r.recommendCount,
        CASE 
            WHEN COUNT(rl2.id) > 0 THEN true 
            ELSE false 
        END,
        r.createdAt
    )
    FROM Review r
    JOIN User u ON r.userId = u.id
    JOIN r.event e
    LEFT JOIN ReviewLike rl2 
        ON rl2.reviewId = r.id AND rl2.userId = :userId
    WHERE e.id = :eventId
    GROUP BY r.id, r.userId, e.id, u.nickname, e.title, r.rating, r.content, r.recommendCount, r.createdAt
    """)
    List<ReviewResponse> findReviews(@Param("eventId") Long eventId, @Param("userId") Long userId);
}
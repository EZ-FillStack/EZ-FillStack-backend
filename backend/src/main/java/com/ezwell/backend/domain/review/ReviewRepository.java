package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // JOIN으로 한번에 가져오기
    @Query("""
    SELECT new com.ezwell.backend.domain.review.dto.ReviewResponse(
        r.id,
        r.memberId,
        r.eventId,
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
    JOIN User u ON r.memberId = u.id
    JOIN Event e ON r.eventId = e.id
    LEFT JOIN ReviewLike rl ON rl.reviewId = r.id
    LEFT JOIN ReviewLike rl2 
        ON rl2.reviewId = r.id AND rl2.memberId = :userId
    WHERE r.eventId = :eventId
    GROUP BY r.id, u.nickname, e.title
""")
    List<ReviewResponse> findReviewsWithLikeAndVoted(Long eventId, Long userId);

    // 중복 체크
    boolean existsByMemberIdAndEventId(Long memberId, Long eventId);
}
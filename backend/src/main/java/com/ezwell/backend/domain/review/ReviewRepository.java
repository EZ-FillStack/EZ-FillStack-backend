package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.review.dto.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndEvent_Id(Long userId, Long eventId);

    // 특정 이벤트의 리뷰 목록 조회
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

    // 프론트엔드 요청 8번: 베스트 리뷰 조회
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
    GROUP BY r.id, r.userId, e.id, u.nickname, e.title, r.rating, r.content, r.recommendCount, r.createdAt
    ORDER BY r.recommendCount DESC
    """)
    List<ReviewResponse> findBestReviews(@Param("userId") Long userId);
    
    // 신청한 이벤트 중 리뷰 작성 여부 확인 (N+1 방지용 일괄 조회)
    @Query("SELECT r.event.id FROM Review r WHERE r.userId = :userId AND r.event.id IN :eventIds")
    Set<Long> findEventIdsByUserIdAndEventIdIn(@Param("userId") Long userId, @Param("eventIds") List<Long> eventIds);

}
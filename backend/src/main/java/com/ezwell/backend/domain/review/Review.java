package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.event.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "recommend_count", nullable = false)
    private int recommendCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Review(Long userId, Event event, int rating, String content) {
        this.userId = userId;
        this.event = event;
        this.rating = rating;
        this.content = content;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 좋아요 증가
    public void increaseRecommend() {
        this.recommendCount++;
    }

    // 좋아요 감소
    public void decreaseRecommend() {
        if (this.recommendCount > 0) {
            this.recommendCount--;
        }
    }
}
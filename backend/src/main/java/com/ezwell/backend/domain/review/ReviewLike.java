package com.ezwell.backend.domain.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "review_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "review_id"})
        }
)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ReviewLike(Long memberId, Long reviewId) {
        this.memberId = memberId;
        this.reviewId = reviewId;
    }
}
package com.ezwell.backend.domain.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long eventId;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    public Review(Long memberId, Long eventId, int rating, String content) {
        this.memberId = memberId;
        this.eventId = eventId;
        this.rating = rating;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
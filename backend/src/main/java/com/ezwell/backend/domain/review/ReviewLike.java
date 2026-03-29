package com.ezwell.backend.domain.review;

import com.ezwell.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "review_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "review_id"})
        }
)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ReviewLike(User user, Review review) {
        this.user = user;
        this.review = review;
    }
}
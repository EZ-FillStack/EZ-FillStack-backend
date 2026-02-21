package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;

import com.ezwell.backend.domain.event.exception.CapacityExceededException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 이벤트 엔티티
 * - 선착순 이벤트
 * - 상태 기반 신청 가능
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 동시에 여러 명이 신청해도 version값이 다르면 업데이트 실패
    @Version
    private Long version;

    @Column(nullable = false)
    private String title;

    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;
    private String placeName;

    private LocalDateTime eventStartDateTime;
    private LocalDateTime eventEndDateTime;

    private LocalDateTime applyStartDateTime;
    private LocalDateTime applyEndDateTime;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentParticipants = 0;

    private Integer viewCount = 0;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Event(String title, Integer capacity, Category category) {
        this.title = title;
        this.capacity = capacity;
        this.category = category;
        this.status = EventStatus.UPCOMING;
        this.createdAt = LocalDateTime.now();
    }

    // 신청 가능 여부 체크
    public void validateApplicable(LocalDateTime now) {

        if (deletedAt != null) {
            throw new IllegalStateException("DELETED_EVENT");
        }

        if (status != EventStatus.OPEN) {
            throw new IllegalStateException("NOT_OPEN");
        }

        if (applyStartDateTime != null && now.isBefore(applyStartDateTime)) {
            throw new IllegalStateException("BEFORE_APPLY_START");
        }

        if (applyEndDateTime != null && now.isAfter(applyEndDateTime)) {
            throw new IllegalStateException("AFTER_APPLY_END");
        }
    }


     // 정원 초과 방지 및 정원 도달 시 자동 마감
    public void increaseParticipants() {
        if (currentParticipants >= capacity) {
            throw new CapacityExceededException();
        }

        this.currentParticipants++;

        if (this.currentParticipants.equals(this.capacity)) {
            this.status = EventStatus.CLOSED;
        }
    }

    // 신청 취소
    public void decreaseParticipants() {
        if (currentParticipants > 0) {
            this.currentParticipants--;
        }
    }

}
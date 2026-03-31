package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String thumbnailUrl;
    private String description;
    private String address;
    private String placeName;


    private LocalDateTime eventStartDateTime;
    private LocalDateTime eventEndDateTime;

    private LocalDateTime applyStartDateTime;
    private LocalDateTime applyEndDateTime;

    private Integer capacity;


    private Integer currentParticipants = 0;
    private Integer bookmarkCount = 0;

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    // 생성자
    public Event(
            String title,
            String thumbnailUrl,
            String description,
            String address,
            String placeName,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            LocalDateTime applyStartDateTime,
            LocalDateTime applyEndDateTime,
            Integer capacity,
            Category category
    ) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.address = address;
        this.placeName = placeName;
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
        this.applyStartDateTime = applyStartDateTime;
        this.applyEndDateTime = applyEndDateTime;
        this.capacity = capacity;
        this.category = category;
        this.status = EventStatus.UPCOMING;
    }

    // =========================
    // 상태 관련
    // =========================

    public void openIfApplicable(LocalDateTime now) {
        if (this.applyStartDateTime != null && now.isAfter(this.applyStartDateTime)) {
            this.status = EventStatus.OPEN;
        }
    }

    public void close() {
        this.status = EventStatus.CLOSED;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    // =========================
    // 업데이트
    // =========================

    public void update(
            String title,
            String thumbnailUrl,
            String description,
            String address,
            String placeName,
            LocalDateTime eventStartDateTime,
            LocalDateTime eventEndDateTime,
            LocalDateTime applyStartDateTime,
            LocalDateTime applyEndDateTime,
            Integer capacity,
            Category category
    ) {
        if (title != null) this.title = title;
        if (thumbnailUrl != null) this.thumbnailUrl = thumbnailUrl;
        if (description != null) this.description = description;
        if (address != null) this.address = address;
        if (placeName != null) this.placeName = placeName;
        if (eventStartDateTime != null) this.eventStartDateTime = eventStartDateTime;
        if (eventEndDateTime != null) this.eventEndDateTime = eventEndDateTime;
        if (applyStartDateTime != null) this.applyStartDateTime = applyStartDateTime;
        if (applyEndDateTime != null) this.applyEndDateTime = applyEndDateTime;
        if (capacity != null) this.capacity = capacity;
        if (category != null) this.category = category;
    }

    // =========================
    // 참가자 관리
    // =========================

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void increaseParticipants() {
        if (this.currentParticipants == null) {
            this.currentParticipants = 0;
        }
        this.currentParticipants++;
    }

    public void decreaseParticipants() {
        if (this.currentParticipants == null || this.currentParticipants == 0) {
            return;
        }
        this.currentParticipants--;
    }

    // =========================
    // 북마크 관리
    // =========================

    public void increaseBookmarkCount() {
        if (this.bookmarkCount == null) {
            this.bookmarkCount = 0;
        }
        this.bookmarkCount++;
    }

    public void decreaseBookmarkCount() {
        if (this.bookmarkCount == null || this.bookmarkCount == 0) {
            return;
        }
        this.bookmarkCount--;
    }
}
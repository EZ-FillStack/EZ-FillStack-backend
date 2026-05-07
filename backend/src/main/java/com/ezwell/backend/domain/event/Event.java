package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "thumbnail_url") // DB 컬럼명 thumbnail_url 매핑
    private String thumbnailUrl;

    private String description;
    private String address;

    @Column(name = "place_name") // DB 컬럼명 place_name 매핑
    private String placeName;

    // DDL 스키마의 'datetime' 형식에 맞게 언더바 위치 조정
    @Column(name = "event_start_datetime")
    private LocalDateTime eventStartDateTime;

    @Column(name = "event_end_datetime")
    private LocalDateTime eventEndDateTime;

    @Column(name = "apply_start_datetime")
    private LocalDateTime applyStartDateTime;

    @Column(name = "apply_end_datetime")
    private LocalDateTime applyEndDateTime;

    private Integer capacity;

    @Column(name = "current_participants") // DB 컬럼명 current_participants 매핑
    private Integer currentParticipants = 0;

    @Column(name = "bookmark_count") // DB 컬럼명 bookmark_count 매핑
    private Integer bookmarkCount = 0;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // FK 컬럼명 명시
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
        this.updatedAt = LocalDateTime.now(); // 업데이트 시 시간 갱신
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

    // =========================
    // 이벤트 신청 상태 관리
    // =========================
    public void validateApplicable(LocalDateTime now) {
    	if(this.deletedAt != null) {
    		throw new IllegalStateException("삭제된 이벤트입니다.");
    	}
    	if(this.status != EventStatus.OPEN) {
    		throw new IllegalStateException("신청 가능한 상태가 아닙니다.");
    	}
    	if(this.applyStartDateTime != null && now.isBefore(this.applyStartDateTime)) {
    		throw new IllegalStateException("신청 기간이 시작되지 않았습니다.");
    	}
    	if(this.applyEndDateTime != null && now.isAfter(this.applyEndDateTime)) {
    		throw new IllegalStateException("신청 기간이 종료되었습니다.");
    	}
    }
}
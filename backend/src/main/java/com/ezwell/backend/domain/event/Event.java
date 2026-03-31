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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    // 생성
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
    }

    // 부분 수정
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
}
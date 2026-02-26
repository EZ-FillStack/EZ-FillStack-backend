package com.ezwell.backend.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventUpdateRequest {

    private String title;
    private String description;
    private Integer capacity;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long categoryId; // 카테고리 변경 가능하도록
}
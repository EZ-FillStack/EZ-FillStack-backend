package com.ezwell.backend.domain.event.dto;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record EventUpdateRequest(

        String title,
        String thumbnailUrl,
        String description,
        String address,
        String placeName,

        LocalDateTime eventStartDateTime,
        LocalDateTime eventEndDateTime,

        LocalDateTime applyStartDateTime,
        LocalDateTime applyEndDateTime,

        @Min(1) Integer capacity,

        Long categoryId
) {}
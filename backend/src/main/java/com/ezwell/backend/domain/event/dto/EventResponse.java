package com.ezwell.backend.domain.event.dto;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventStatus;

import java.time.LocalDateTime;

public record EventResponse(

        Long id,
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
        Integer currentParticipants,
        Integer viewCount,
        EventStatus status,
        Long categoryId
) {

    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getThumbnailUrl(),
                event.getDescription(),
                event.getAddress(),
                event.getPlaceName(),
                event.getEventStartDateTime(),
                event.getEventEndDateTime(),
                event.getApplyStartDateTime(),
                event.getApplyEndDateTime(),
                event.getCapacity(),
                event.getCurrentParticipants(),
                event.getViewCount(),
                event.getStatus(),
                event.getCategory() != null ? event.getCategory().getId() : null
        );
    }
}
package com.ezwell.backend.domain.event.dto;

import com.ezwell.backend.domain.event.Event;

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

        Integer capacity,
        Long categoryId
) {
    public static EventResponse from(Event e) {
        return new EventResponse(
                e.getId(),
                e.getTitle(),
                e.getThumbnailUrl(),
                e.getDescription(),
                e.getAddress(),
                e.getPlaceName(),
                e.getEventStartDateTime(),
                e.getEventEndDateTime(),
                e.getCapacity(),
                e.getCategory().getId()
        );
    }
}
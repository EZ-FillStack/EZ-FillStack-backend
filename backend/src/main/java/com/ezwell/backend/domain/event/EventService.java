package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    // 전체 조회
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(event -> event.getDeletedAt() == null)
                .map(EventResponse::from)
                .toList();
    }

    // 단건 조회
    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

        if (event.getDeletedAt() != null) {
            throw new IllegalStateException("DELETED_EVENT");
        }

        return EventResponse.from(event);
    }
}
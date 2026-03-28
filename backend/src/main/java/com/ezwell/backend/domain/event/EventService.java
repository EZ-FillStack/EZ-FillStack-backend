package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAllByOrderByCreatedAtDesc()
          .stream()
          .filter(event -> event.getDeletedAt() == null)
          .map(EventResponse::from)
          .toList();
    }

    public EventResponse getEvent(Long id) {
        Event event = findEventById(id);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {
        Event event = new Event(
          request.title(),
          request.capacity(),
          null
        );

        return EventResponse.from(eventRepository.save(event));
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventUpdateRequest request) {
        Event event = findEventById(id);

        event.update(
          request.getTitle(),
          null, // thumbnailUrl (UpdateRequest에 없다면 기존값 유지 혹은 null)
          request.getDescription(),
          null, // address
          null, // placeName
          request.getStartAt(), // eventStartDateTime
          request.getEndAt(),   // eventEndDateTime
          null, // applyStartDateTime
          null, // applyEndDateTime
          request.getCapacity(),
          null  // category
        );

        return EventResponse.from(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = findEventById(id);
        event.markDeleted();
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
          .filter(event -> event.getDeletedAt() == null)
          .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));
    }
}
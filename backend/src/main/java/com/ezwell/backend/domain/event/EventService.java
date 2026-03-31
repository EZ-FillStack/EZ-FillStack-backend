package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;
import com.ezwell.backend.domain.category.CategoryRepository;
import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    // 생성
    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        Event event = new Event(
                request.title(),
                request.thumbnailUrl(),
                request.description(),
                request.address(),
                request.placeName(),
                request.eventStartDateTime(),
                request.eventEndDateTime(),
                request.applyStartDateTime(),
                request.applyEndDateTime(),
                request.capacity(),
                category
        );

        eventRepository.save(event);

        return EventResponse.from(event);
    }

    // 수정
    @Transactional
    public EventResponse updateEvent(Long eventId, EventUpdateRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        event.update(
                request.title(),
                request.thumbnailUrl(),
                request.description(),
                request.address(),
                request.placeName(),
                request.eventStartDateTime(),
                request.eventEndDateTime(),
                request.applyStartDateTime(),
                request.applyEndDateTime(),
                request.capacity(),
                category
        );

        return EventResponse.from(event);
    }

    // 삭제 (soft delete)
    @Transactional
    public void deleteEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        event.delete();
    }
}
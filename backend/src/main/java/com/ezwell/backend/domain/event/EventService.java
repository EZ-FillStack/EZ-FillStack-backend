package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;
import com.ezwell.backend.domain.category.CategoryRepository;
import com.ezwell.backend.domain.event.dto.*;
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
    public EventResponse create(EventCreateRequest request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

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

        return EventResponse.from(eventRepository.save(event));
    }

    // 수정
    @Transactional
    public EventResponse update(Long id, EventUpdateRequest request) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이벤트 없음"));

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));
        }

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
}
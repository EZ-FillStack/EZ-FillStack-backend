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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    // 이벤트 목록 조회
    public List<EventResponse> getEvents(String sort, String status, Long categoryId) {
        List<Event> events;

        if (categoryId != null) {
            events = eventRepository.findByCategoryId(categoryId);
        } else if ("upcoming".equals(status)) {
            events = eventRepository.findByEventStartDateTimeAfter(LocalDateTime.now());
        } else if ("popular".equals(sort)) {
            events = eventRepository.findAllByOrderByBookmarkCountDesc();
        } else {
            events = eventRepository.findAll();
        }

        return events.stream()
                // deletedAt이 null인  데이터만 필터링
                .filter(event -> event.getDeletedAt() == null)
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }

    // 이벤트 상세 조회
    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(EventNotFoundException::new);

        //  이미 삭제된 이벤트의 ID로 접근하면 없는 이벤트로 처리
        if (event.getDeletedAt() != null) {
            throw new EventNotFoundException();
        }

        return EventResponse.from(event);
    }

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

        // 삭제된 이벤트는 수정할 수 없도록 차단
        if (event.getDeletedAt() != null) {
            throw new EventNotFoundException();
        }

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

    // 삭제
    @Transactional
    public void deleteEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        // 이미 삭제된 이벤트를 또 삭제 요청할 경우의 방지 로직
        if (event.getDeletedAt() != null) {
            throw new EventNotFoundException();
        }

        event.delete();
    }
}
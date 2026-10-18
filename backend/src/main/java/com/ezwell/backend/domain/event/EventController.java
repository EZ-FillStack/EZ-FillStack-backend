package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List; // (추가)

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getEvents(
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) Long categoryId
    ) {
        return eventService.getEvents(sort, status, categoryId);
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }


    // 생성
    @PostMapping
    public EventResponse create(@RequestBody EventCreateRequest request) {
        return eventService.createEvent(request);
    }

    // 수정
    @PatchMapping("/{id}")
    public EventResponse update(
            @PathVariable Long id,
            @RequestBody EventUpdateRequest request
    ) {
        return eventService.updateEvent(id, request);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
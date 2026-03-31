package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

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
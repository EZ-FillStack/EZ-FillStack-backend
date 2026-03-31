package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public EventResponse create(@RequestBody EventCreateRequest request) {
        return eventService.create(request);
    }

    @PatchMapping("/{id}")
    public EventResponse update(
            @PathVariable Long id,
            @RequestBody EventUpdateRequest request
    ) {
        return eventService.update(id, request);
    }
}
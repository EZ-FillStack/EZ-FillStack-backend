package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    @PostMapping
    public EventResponse create(@Valid @RequestBody EventCreateRequest request) {
        return eventService.createEvent(request);
    }

    @PostMapping("/{id}/apply")
    public EventResponse apply(@PathVariable Long id) {
        return eventService.applyToEvent(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        eventService.cancelApply(id);
    }

    @GetMapping("/me")
    public List<EventResponse> myEvents() {
        return eventService.myEvents();
    }
}
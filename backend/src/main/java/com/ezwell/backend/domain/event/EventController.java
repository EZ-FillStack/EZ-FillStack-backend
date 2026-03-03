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

    // 전체 조회
    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    // 이벤트 신청
    @PostMapping("/{id}/apply")
    public EventResponse apply(@PathVariable Long id) {
        return eventService.applyToEvent(id);
    }

    // 신청 취소
    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        eventService.cancelApply(id);
    }

    // 내 신청 목록
    @GetMapping("/me")
    public List<EventResponse> myEvents() {
        return eventService.myEvents();
    }
}
package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.event.EventService;
import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")

// ADMIN만 접근 가능
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventController {

    private final EventService eventService;

    // 이벤트 생성
    @PostMapping
    public EventResponse create(@RequestBody EventCreateRequest request) {
        return eventService.createEvent(request);
    }

    // 이벤트 수정
    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id,
                                @RequestBody EventUpdateRequest request) {
        return eventService.updateEvent(id, request);
    }

    // 이벤트 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
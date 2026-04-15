package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEventController {

    private final AdminService adminService;

    //관리자 이벤트 생성
    @PostMapping
    public EventResponse create(@RequestBody EventCreateRequest request) {
        return adminService.createEvent(request);
    }

    //관리자 이벤트 수정
    @PatchMapping("/{id}")
    public EventResponse update(@PathVariable Long id,
                                @RequestBody EventUpdateRequest request) {
        return adminService.updateEvent(id, request);
    }

    //관리자 이벤트 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminService.deleteEvent(id);
    }
}
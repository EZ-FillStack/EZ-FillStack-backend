package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import com.ezwell.backend.domain.application.dto.ApplicationResponse; // (추가: DTO 패키지명 확인 필요)
import java.util.List;
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

    // 신청 전체 조회
    @GetMapping("/applications")
    public List<ApplicationResponse> getAllApplications() {
        return adminService.getAllApplications();
    }

    // 특정 이벤트의 신청자 목록 조회
    @GetMapping("/events/{eventId}/applications")
    public List<ApplicationResponse> getApplicationsByEvent(@PathVariable Long eventId) {
        return adminService.getApplicationsByEvent(eventId);
    }

    // 신청 상태 변경 (승인/거절 등)
    @PatchMapping("/applications/{applicationId}/status")
    public void updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status // (참고: PENDING, APPROVED, REJECTED 등)
    ) {
        adminService.updateApplicationStatus(applicationId, status);
    }

    // 신청 강제 취소
    @DeleteMapping("/applications/{applicationId}")
    public void deleteApplication(@PathVariable Long applicationId) {
        adminService.deleteApplication(applicationId);
    }
}
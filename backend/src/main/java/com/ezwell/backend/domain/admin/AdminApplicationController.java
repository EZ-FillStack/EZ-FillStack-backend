package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.application.ApplicationService;
import com.ezwell.backend.domain.application.dto.ApplicationResponse;
import com.ezwell.backend.domain.application.dto.ApplicationStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApplicationController {

    private final ApplicationService applicationService;

    // 신청 전체 조회
    @GetMapping("/applications")
    public List<ApplicationResponse> getAllApplications() {
        return applicationService.getAllApplications();
    }

    // 특정 이벤트 신청 조회
    @GetMapping("/events/{eventId}/applications")
    public List<ApplicationResponse> getApplicationsByEvent(@PathVariable Long eventId) {
        return applicationService.getApplicationsByEvent(eventId);
    }

    // 상태 변경
    @PatchMapping("/applications/{applicationId}/status")
    public void updateStatus(@PathVariable Long applicationId,
                             @RequestBody ApplicationStatusRequest request) {
        applicationService.updateApplicationStatus(applicationId, request);
    }

    // 강제 취소
    @DeleteMapping("/applications/{applicationId}")
    public void delete(@PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
    }
}
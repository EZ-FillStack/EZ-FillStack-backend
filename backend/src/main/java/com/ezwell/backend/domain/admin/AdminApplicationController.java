package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.application.dto.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApplicationController {

    private final AdminService adminService;

    //신청 전체 조회)
    @GetMapping("/applications")
    public List<ApplicationResponse> getAllApplications() {
        return adminService.getAllApplications();
    }

    //특정 이벤트 신청 조회
    @GetMapping("/events/{eventId}/applications")
    public List<ApplicationResponse> getApplicationsByEvent(@PathVariable Long eventId) {
        return adminService.getApplicationsByEvent(eventId);
    }

    //상태 변경
    @PatchMapping("/applications/{applicationId}/status")
    public void updateStatus(@PathVariable Long applicationId, @RequestParam String status) {
        adminService.updateApplicationStatus(applicationId, status);
    }

    //강제 취소
    @DeleteMapping("/applications/{applicationId}")
    public void delete(@PathVariable Long applicationId) {
        adminService.deleteApplication(applicationId);
    }
}
package com.ezwell.backend.domain.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezwell.backend.domain.application.dto.AdminApplicationResponse;
import com.ezwell.backend.domain.application.dto.ApplicationStatusRequest;
import com.ezwell.backend.domain.application.dto.MyApplicationResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ApplicationController {
	
	private final ApplicationService applicationService;
	
	// 행사 신청
	@PostMapping("/events/{eventId}/applications")
	public ResponseEntity<String> apply(@PathVariable("eventId") Long eventId){
		applicationService.applyEvent(eventId);
		return ResponseEntity.ok("신청이 완료되었습니다.");
	}
	
	// 신청 취소
	@DeleteMapping("/events/applications/{eventId}")
	public ResponseEntity<Void> cancel(@PathVariable("eventId") Long eventId){
		applicationService.cancelApplication(eventId);
		return ResponseEntity.noContent().build();
	}
	
	// 신청 목록
    @GetMapping("/me/applications")
    public ResponseEntity<List<MyApplicationResponse>> getMyList() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }
    
    /// 관리자
    
    // 신청 전체 조회
    @GetMapping("/admin/applications")
    public ResponseEntity<List<AdminApplicationResponse>> getAllApplications() {
    	return ResponseEntity.ok(applicationService.getAllApplications());
    }
    
    // 특정 신청 조회
    @GetMapping("/admin/events/{eventId}/applications")
    public ResponseEntity<List<AdminApplicationResponse>> getApplicationsByEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(applicationService.getApplicationsByEvent(eventId));
    }
    
    // 신청 상태 변경
    @PatchMapping("/admin/applications/{applicationId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long applicationId,
    										 @RequestBody ApplicationStatusRequest request){
    	applicationService.updateApplicationStatus(applicationId, request);
    	return ResponseEntity.ok().build();
    }
    
    // 신청 강제 취소
    @DeleteMapping("/admin/applications/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId){
    	applicationService.deleteApplication(applicationId);
    	return ResponseEntity.noContent().build();
    }
    
}
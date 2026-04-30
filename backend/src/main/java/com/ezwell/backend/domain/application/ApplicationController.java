package com.ezwell.backend.domain.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezwell.backend.domain.application.dto.MyApplicationResponse;

import lombok.RequiredArgsConstructor;

@RestController
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
	@DeleteMapping("/events/applications/{applicationId}")
	public ResponseEntity<Void> cancel(@PathVariable("applicationId") Long applicationId){
		applicationService.cancelApplication(applicationId);
		return ResponseEntity.noContent().build();
	}
	
	// 신청 목록
    @GetMapping("/me/applications")
    public ResponseEntity<List<MyApplicationResponse>> getMyList() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }
}
package com.ezwell.backend.domain.application.dto;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyApplicationResponse {
	private Long applicationId;
	private Long eventId;
	private String eventTitle;
	private ApplicationStatus status;
	private LocalDateTime appliedAt;
	
	public static MyApplicationResponse from(Application application) {
		return new MyApplicationResponse (
			application.getId(),
			application.getEvent().getId(),
			application.getEvent().getTitle(),
			application.getStatus(),
			application.getAppliedAt()
		);
	}
}

package com.ezwell.backend.domain.application.dto;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponse {
	private String message;
	private ApplicationStatus status;
	
	// 신청/취소 결과용
	public static ApplicationResponse of(String message, Application application) {
		return new ApplicationResponse(
				message,
				application.getStatus()
		);
	}
}

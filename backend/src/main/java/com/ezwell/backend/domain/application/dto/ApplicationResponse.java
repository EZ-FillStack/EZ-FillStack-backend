package com.ezwell.backend.domain.application.dto;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApplicationResponse {
	private Long id;
	private String userNickname;
	private String eventTitle;
	private ApplicationStatus status;
	private LocalDateTime appliedAt;
	private String message;

	// 관리자에서 목록 조회에서 사용할 변환 메서드
	public static ApplicationResponse from(Application app) {
		return new ApplicationResponse(
				app.getId(),
				app.getUser().getNickname(),
				app.getEvent().getTitle(),
				app.getStatus(),
				app.getAppliedAt(),
				null
		);
	}

	// 신청/취소 결과용
	public static ApplicationResponse of(String message, Application application) {
		return new ApplicationResponse(
				application.getId(),
				application.getUser().getNickname(),
				application.getEvent().getTitle(),
				application.getStatus(),
				application.getAppliedAt(),
				message
		);
	}
}
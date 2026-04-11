package com.ezwell.backend.domain.application.dto;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationResponse {
	private Long id;
	private Long eventId;
	private String eventTitle;
	private Long userId;
	private String userNickname;
	private String userEmail;
	private ApplicationStatus status;
	private LocalDateTime appliedAt;
	private String message;

	// 관리자 및 일반 목록 조회 통합 변환 메서드
	public static ApplicationResponse from(Application app) {
		return ApplicationResponse.builder()
				.id(app.getId())
				.eventId(app.getEvent().getId())
				.eventTitle(app.getEvent().getTitle())
				.userId(app.getUser().getId())
				.userNickname(app.getUser().getNickname())
				.userEmail(app.getUser().getEmail())
				.status(app.getStatus())
				.appliedAt(app.getAppliedAt())
				.build();
	}

	// 신청/취소 결과 메시지용
	public static ApplicationResponse of(String message, Application app) {
		return ApplicationResponse.builder()
				.id(app.getId())
				.eventId(app.getEvent().getId())
				.eventTitle(app.getEvent().getTitle())
				.userId(app.getUser().getId())
				.userNickname(app.getUser().getNickname())
				.userEmail(app.getUser().getEmail())
				.status(app.getStatus())
				.appliedAt(app.getAppliedAt())
				.message(message)
				.build();
	}
}
package com.ezwell.backend.domain.application.dto;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminApplicationResponse {

	private Long applicationId;
	private Long eventId;
	private String eventTitle;
	private Long userId;
	private String userNickname;
	private String userEmail;
	private ApplicationStatus status;
	private LocalDateTime appliedAt;
	
	public static AdminApplicationResponse  from(Application application) {
        return AdminApplicationResponse.builder()
                .applicationId(application.getId())
                .eventId(application.getEvent().getId())
                .eventTitle(application.getEvent().getTitle())
                .userId(application.getUser().getId())
                .userNickname(application.getUser().getNickname())
                .userEmail(application.getUser().getEmail())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .build();
	}
}

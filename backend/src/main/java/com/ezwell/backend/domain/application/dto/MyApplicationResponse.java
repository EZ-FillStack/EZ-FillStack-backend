package com.ezwell.backend.domain.application.dto;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MyApplicationResponse {
	private Long applicationId;
	private Long eventId;
	private String eventTitle;
	private ApplicationStatus status;
	private boolean hasReview;
	private String thumbnailUrl;
	private String placeName;
    private LocalDateTime eventStartDateTime;
	private LocalDateTime appliedAt;
	
    public static MyApplicationResponse of(Application application, boolean hasReview) {
        return MyApplicationResponse.builder()
                .applicationId(application.getId())
                .eventId(application.getEvent().getId())
                .eventTitle(application.getEvent().getTitle())
                .status(application.getStatus())
                .hasReview(hasReview)
                .thumbnailUrl(application.getEvent().getThumbnailUrl())
                .placeName(application.getEvent().getPlaceName())
                .eventStartDateTime(application.getEvent().getEventStartDateTime()) // Event 필드명 맞춤
                .appliedAt(application.getAppliedAt())
                .build();
    }
}

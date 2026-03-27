package com.ezwell.backend.domain.notification.dto;

import com.ezwell.backend.domain.notification.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long id;
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private Boolean isSent;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .eventId(notification.getEvent().getId())
                .eventTitle(notification.getEvent().getTitle())
                .isSent(notification.getIsSent())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
package com.ezwell.backend.domain.notification;

import com.ezwell.backend.domain.notification.dto.NotificationResponse;
import com.ezwell.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 내 알림 조회 (로그인 기반)
    @GetMapping
    public List<NotificationResponse> getMyNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();

        return notificationService.getNotificationsByUser(userId);
    }

    // 알림 생성
    @PostMapping("/events/{eventId}")
    public void createNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long eventId) {

        Long userId = userDetails.getUserId();

        notificationService.createNotification(userId, eventId);
    }

    // 알림 발송 처리
    @PatchMapping("/{notificationId}/sent")
    public void markAsSent(@PathVariable Long notificationId) {
        notificationService.markAsSent(notificationId);
    }

    // 미발송 알림 (관리자/스케줄러용)
    @GetMapping("/pending")
    public List<NotificationResponse> getPendingNotifications() {
        return notificationService.getPendingNotifications();
    }
}
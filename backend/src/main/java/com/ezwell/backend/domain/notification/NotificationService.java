package com.ezwell.backend.domain.notification;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.notification.dto.NotificationResponse;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    // 특정 유저 알림 전체 조회
    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return notificationRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    // 알림 생성
    @Transactional
    public void createNotification(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

        if (notificationRepository.existsByUserAndEvent(user, event)) {
            throw new IllegalStateException("NOTIFICATION_ALREADY_EXISTS");
        }

        Notification notification = new Notification(user, event);
        notificationRepository.save(notification);
    }

    // 알림 발송 완료 처리
    @Transactional
    public void markAsSent(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("NOTIFICATION_NOT_FOUND"));

        if (!notification.getIsSent()) {
            notification.markSent();
        }
    }

    // 발송 안 된 알림 목록 조회 (스케줄러/관리자용)
    public List<NotificationResponse> getPendingNotifications() {
        return notificationRepository.findAllByIsSentFalseOrderByCreatedAtAsc()
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
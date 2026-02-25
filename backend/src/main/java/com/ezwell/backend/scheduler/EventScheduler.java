package com.ezwell.backend.scheduler;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.event.EventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;

    /**
     * 1분마다 이벤트 상태 자동 업데이트
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateEventStatus() {

        LocalDateTime now = LocalDateTime.now();

        List<Event> events = eventRepository.findAll();

        for (Event event : events) {

            if (event.getDeletedAt() != null) continue;

            // 신청 시작 → OPEN
            if (event.getStatus() == EventStatus.UPCOMING) {
                event.openIfApplicable(now);
            }

            // 신청 종료 → CLOSED
            if (event.getApplyEndDateTime() != null
                    && now.isAfter(event.getApplyEndDateTime())
                    && event.getStatus() != EventStatus.CLOSED) {

                event.close();
            }

            // 이벤트 종료 시 자동 마감
            if (event.getEventEndDateTime() != null
                    && now.isAfter(event.getEventEndDateTime())) {

                event.close();
            }
        }
    }
}
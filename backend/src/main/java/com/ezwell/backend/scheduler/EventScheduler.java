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

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateEventStatus() {

        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAll();

        for (Event event : events) {

            if (event.getDeletedAt() != null) {
                continue;
            }

            if (event.getStatus() == EventStatus.UPCOMING) {
                event.openIfApplicable(now);
            }

            if (event.getApplyEndDateTime() != null
                    && now.isAfter(event.getApplyEndDateTime())
                    && event.getStatus() == EventStatus.OPEN) {
                event.close();
            }

            if (event.getEventEndDateTime() != null
                    && now.isAfter(event.getEventEndDateTime())
                    && event.getStatus() != EventStatus.CLOSED) {
                event.close();
            }
        }
    }
}
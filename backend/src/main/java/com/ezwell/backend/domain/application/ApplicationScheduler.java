package com.ezwell.backend.domain.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationScheduler {

	private EventRepository eventRepository;
	private ApplicationRepository appliationRepository;
	
	// 매일 자정 실행
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void completeFinishedApplications() {
		LocalDateTime now = LocalDateTime.now();
		
		// 종료일이 지난 이벤트 조회
		List<Event> finishedEvents = eventRepository.findByEventEndDateTimeBefore(now);
		
		for(Event event: finishedEvents) {
			List<Application> application = appliationRepository.findByEventAndStatus(event, ApplicationStatus.APPROVED);
			
			// 전부 COMPLETED로 전환
			application.forEach(Application::complete);
		}
	}
}

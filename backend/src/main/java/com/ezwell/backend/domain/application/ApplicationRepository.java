package com.ezwell.backend.domain.application;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.user.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	// 중복체크
	boolean existsByUserAndEvent(User user, Event event);
	
	// 신청 목록 조회
	@EntityGraph(attributePaths = {"event"})
	List<Application> findAllByUserAndStatusNotOrderByAppliedAtDesc(User user, ApplicationStatus status);
	
    // 특정 이벤트 신청 목록
    List<Application> findByEventId(Long eventId);
}

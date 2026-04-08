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
	List<Application> findAllByUserOrderByAppliedAtDesc(User user);
	
	/// 관리자
	
	// 전체 신청 목록 (user & event)
	@EntityGraph(attributePaths = {"user","event"})
	List<Application> findAllByOrderByAppliedAtDesc();
	
	// 특정 신청 목록
	@EntityGraph(attributePaths = {"user","event"})
	List<Application> findAllByEventOrderByAppliedAtDesc(Event event);
	
}

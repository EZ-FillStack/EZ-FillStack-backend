package com.ezwell.backend.domain.application;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Application {

	// 고유값 자동생성
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 신청한 유저
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	// 신청 대상의 이벤트
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;
	
	// 진행사항
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	// 이벤트 신청
	@Builder
	public Application(User user, Event event) {
		this.user = user;
		this.event = event;
		this.status = ApplicationStatus.PENDING;
	}
	
	// 신청 취소
	public void cancel() {
		this.status = ApplicationStatus.CANCELED;
	}
}

/*

*/
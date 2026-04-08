package com.ezwell.backend.domain.application;

import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="applications",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","event_id"})})
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;
	
	// 진행사항
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	private LocalDateTime appliedAt;
	
	// 이벤트 신청
	@Builder
	public Application(User user, Event event) {
		this.user = user;
		this.event = event;
		this.status = ApplicationStatus.PENDING;
		this.appliedAt = LocalDateTime.now();
	}
	
	// 신청 취소
	public void cancel() {
		this.status = ApplicationStatus.CANCELED;
	}
	
    // 관리자 상태 변경 (APPROVED & REJECTED)
    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }
    
}
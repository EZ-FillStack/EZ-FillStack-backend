package com.ezwell.backend.domain.inquiry;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry {
	// 답변 필드 추가 필요 (answerContent, answeredAt)

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// 질문 제목
    private String title;
	
	// 질문 내용
	@Column(columnDefinition = "TEXT", nullable =false)
	private String content;
	
	// 답변 내용
	@Column(columnDefinition = "TEXT")
	private String answerContent;
	
	// 질문 상태
	@Enumerated(EnumType.STRING)
	private InquiryStatus status;
	
	// 질문 시간
	private LocalDateTime createdAt;
	private LocalDateTime answeredAt;
	
	@Builder
	public Inquiry(User user, String content) {
		this.user = user;
		this.content = content;
		this.status = InquiryStatus.UNANSERED;
		this.createdAt = LocalDateTime.now();
	}
	
	// 관리자가 답변
	public void answer(String answerContent) {
		this.answerContent = answerContent;
		this.status = InquiryStatus.ANSERED;
		this.answeredAt = LocalDateTime.now();
	}
}

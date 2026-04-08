package com.ezwell.backend.domain.inquiry;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.inquiry.exception.InquiryException;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="inquiries")
public class Inquiry {
	
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
	@Column(name="reply_email",columnDefinition = "TEXT")
	private String replyEmail;
	
	// 질문 상태
	@Enumerated(EnumType.STRING)
	private InquiryStatus status;
	
	// 질문 시간
	private LocalDateTime createdAt;
	private LocalDateTime answeredAt;
	
	@Builder
	public Inquiry(User user, String title, String content) {
		this.user = user;
	    this.title = title;
		this.content = content;
		this.status = InquiryStatus.UNANSWERED;
		this.createdAt = LocalDateTime.now();
	}
	
	// 관리자 답변 이메일 전송
	public void answer(String replyEmail) {
		this.replyEmail = replyEmail;
		this.status = InquiryStatus.ANSWERED;
		this.answeredAt = LocalDateTime.now();
	}
	
	// 질문 수정
	public void updateInquiry(String title, String content) {
		if(this.status == InquiryStatus.ANSWERED) {
			throw new InquiryException("이미 답변이 완료된 문의는 수정할 수 없습니다.");
		}
		this.title = title;
		this.content = content;
	}
	
}

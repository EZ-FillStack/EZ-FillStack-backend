package com.ezwell.backend.domain.inquiry.dto;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.inquiry.Inquiry;
import com.ezwell.backend.domain.inquiry.InquiryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InquiryResponse {
	private Long id;
	private String title;
	private String content;
	private InquiryStatus status;
	private String replyEmail;
	private LocalDateTime createdAt;
	private LocalDateTime answeredAt;
	
	public static InquiryResponse from(Inquiry inquiry) {
		return InquiryResponse.builder()
				.id(inquiry.getId())
				.title(inquiry.getTitle())
				.content(inquiry.getContent())
				.status(inquiry.getStatus())
				.replyEmail(inquiry.getReplyEmail())
				.createdAt(inquiry.getCreatedAt())
				.answeredAt(inquiry.getAnsweredAt())
				.build();
	}
}

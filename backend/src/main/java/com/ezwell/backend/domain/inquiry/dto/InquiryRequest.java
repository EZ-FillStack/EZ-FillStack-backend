package com.ezwell.backend.domain.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequest {
	private String title;
	private String content;
	private String answerContent; // 관리자 답변
}

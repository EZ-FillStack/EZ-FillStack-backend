package com.ezwell.backend.domain.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatus {
	ANSERED("답변 완료"),
	UNANSERED("답변 대기");
	
	private final String description; 
}

package com.ezwell.backend.domain.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    
    APPROVED("승인"),
    CANCELED("취소"),
    REJECTED("거절"),
	COMPLETED("완료");

    private final String description;

}

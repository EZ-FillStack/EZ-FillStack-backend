package com.ezwell.backend.domain.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum ApplicationStatus {
    
    PENDING("대기"),
    APPROVED("승인"),
    CANCELED("취소"),
    REJECTED("거절");

    private final String description;

}

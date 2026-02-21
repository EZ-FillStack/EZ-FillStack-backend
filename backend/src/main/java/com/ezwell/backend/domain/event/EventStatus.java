package com.ezwell.backend.domain.event;

// EventStatus 신청 전, 신청 중, 신청 마감, 신청 종료와 관한 상황 고정 //

public enum EventStatus {

    UPCOMING,
    OPEN,
    CLOSED,
    FINISHED
}
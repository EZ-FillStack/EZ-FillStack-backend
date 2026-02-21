package com.ezwell.backend.domain.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
        super("행사를 찾을 수 없습니다.");
    }
}
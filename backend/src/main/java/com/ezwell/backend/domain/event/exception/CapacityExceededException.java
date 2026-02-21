package com.ezwell.backend.domain.event.exception;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException() {
        super("정원이 초과되었습니다.");
    }
}

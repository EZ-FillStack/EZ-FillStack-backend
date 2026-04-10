package com.ezwell.backend.global;

import com.ezwell.backend.domain.application.exception.ApplicationEventException;
import com.ezwell.backend.domain.bookmark.exception.BookmarkedException;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.inquiry.exception.InquiryException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(String code, String message) {}

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> eventNotFound(EventNotFoundException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse("EVENT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalState(IllegalStateException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArg(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(InquiryException.class)
    public ResponseEntity<ErrorResponse> handleInquiryException(InquiryException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("INQUIRY_ERROR", e.getMessage()));
    }

    @ExceptionHandler(BookmarkedException.class)
    public ResponseEntity<ErrorResponse> handleBookmarkedException(BookmarkedException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("BOOKMARK_ERROR", e.getMessage()));
    }

    @ExceptionHandler(ApplicationEventException.class)
    public ResponseEntity<ErrorResponse> handleApplicationEventException(ApplicationEventException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("APPLICATION_ERROR", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(new ErrorResponse("FORBIDDEN", "해당 작업에 대한 권한이 없습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception e) {

        e.printStackTrace();

        return ResponseEntity.status(500)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
    }
}
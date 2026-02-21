package com.ezwell.backend.domain.event.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventCreateRequest(

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        String thumbnailUrl,

        String description,

        String address,

        String placeName,

        @NotNull(message = "이벤트 시작 시간은 필수입니다.")
        LocalDateTime eventStartDateTime,

        @NotNull(message = "이벤트 종료 시간은 필수입니다.")
        LocalDateTime eventEndDateTime,

        @NotNull(message = "신청 시작 시간은 필수입니다.")
        LocalDateTime applyStartDateTime,

        @NotNull(message = "신청 종료 시간은 필수입니다.")
        LocalDateTime applyEndDateTime,

        @NotNull(message = "정원은 필수입니다.")
        @Min(value = 1, message = "정원은 최소 1명 이상이어야 합니다.")
        Integer capacity,

        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId
) {
}
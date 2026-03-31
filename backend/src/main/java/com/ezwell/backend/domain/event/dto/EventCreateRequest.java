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

        @NotNull LocalDateTime eventStartDateTime,
        @NotNull LocalDateTime eventEndDateTime,

        @NotNull LocalDateTime applyStartDateTime,
        @NotNull LocalDateTime applyEndDateTime,

        @NotNull @Min(1) Integer capacity,

        @NotNull Long categoryId
) {}
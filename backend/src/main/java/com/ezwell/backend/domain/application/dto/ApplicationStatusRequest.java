package com.ezwell.backend.domain.application.dto;

import com.ezwell.backend.domain.application.ApplicationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationStatusRequest {
	private ApplicationStatus status;
}

package com.ezwell.backend.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	// Enum 상수는 세미콜론(;) 전, 최상단에 선언
	USER("ROLE_USER", "일반 사용자"),
	ADMIN("ROLE_ADMIN", "관리자");

	private final String key;
	private final String title;
}
package com.ezwell.backend.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtSecurityTest {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Test
	@DisplayName("JWT 토큰 생성 및 검증 테스트")
	void createAndValidateTokenTest() {
		// 1. 토큰 생성
		String token = jwtTokenProvider.createAccessToken(1L, "test@test.com", "ROLE_USER");
		assertNotNull(token);

		// 2. 토큰 검증
		assertTrue(jwtTokenProvider.isValid(token));

		// 3. 토큰에서 데이터 추출 및 확인
		String email = jwtTokenProvider.getUserEmail(token);
		assertEquals("test@test.com", email);
	}
}
package com.ezwell.backend.domain.auth.dto;

import jakarta.validation.constraints.*;

public record SignupRequest(
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
	String username, // 추가된 필드

	@Email(message = "올바른 이메일 형식이어야 합니다.")
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
	String password,

	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	String nickname,

	@NotBlank(message = "전화번호는 필수 입력 값입니다.")
	String phone
) {}
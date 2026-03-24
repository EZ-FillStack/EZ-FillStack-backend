package com.ezwell.backend.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @Email(message = "올바른 이메일 형식이어야 합니다.")
				@NotBlank(message = "이메일은 필수 입력 값입니다.")
				String email,

				@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
				@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
				@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
					message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
				String password,
			String nickname,
			String phone
) {}
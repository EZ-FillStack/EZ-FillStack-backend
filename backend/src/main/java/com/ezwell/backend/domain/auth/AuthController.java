package com.ezwell.backend.domain.auth;

import com.ezwell.backend.domain.auth.dto.AuthResponse;
import com.ezwell.backend.domain.auth.dto.ForgotPasswordRequest;
import com.ezwell.backend.domain.auth.dto.LoginRequest;
import com.ezwell.backend.domain.auth.dto.ResetPasswordRequest;
import com.ezwell.backend.domain.auth.dto.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup") //회원가입 post 요청 처리
    public void signup(@Valid @RequestBody SignupRequest req) {authService.signup(req);
    }

    @PostMapping("/login") //로그인 성공 시 JWT 반환
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    // 비밀번호 찾기
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request.email());
    }

    // 비밀번호 재설정
    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());
    }
}
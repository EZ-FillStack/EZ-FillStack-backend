package com.ezwell.backend.domain.auth;

import com.ezwell.backend.domain.auth.dto.AuthResponse;
import com.ezwell.backend.domain.auth.dto.LoginRequest;
import com.ezwell.backend.domain.auth.dto.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController //반환값을 JSON으로 자동 변환
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {this.authService = authService;
    }

    @PostMapping("/signup") //회원가입 post 요청 처리
    public void signup(@Valid @RequestBody SignupRequest req) {authService.signup(req);
    }

    @PostMapping("/login") //로그인 성공 시 JWT 반환
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
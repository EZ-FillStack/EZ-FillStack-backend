package com.ezwell.backend.domain.auth;

import com.ezwell.backend.domain.auth.dto.AuthResponse;
import com.ezwell.backend.domain.auth.dto.LoginRequest;
import com.ezwell.backend.domain.auth.dto.SignupRequest;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //회원가입
    @Transactional
    public void signup(SignupRequest req) {
        //email 여부 확인
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalStateException("EMAIL_ALREADY_EXISTS");
        }
        String hash = passwordEncoder.encode(req.password()); //비밀번호 암호화
        // 생성자에서 자동으로 Role.USER 들어가게 변경됨
        userRepository.save(
                new User(req.email(), hash, req.nickname(), req.phone())
        );
    }

    //로그인
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        //사용자 조회
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalStateException("INVALID_CREDENTIALS"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalStateException("INVALID_CREDENTIALS");
        }

        //JWT 생성
        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }

    // 비밀번호 찾기: resetToken 발급
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        // 간단 토큰 생성 (UUID)
        String token = UUID.randomUUID().toString();

        user.setResetToken(token);

        // 원래는 이메일 보내야 함 (지금은 반환)
        return token;
    }

    //비밀번호 재설정
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_TOKEN"));

        // 비밀번호 암호화 필요 (예: BCrypt)
        String encoded = passwordEncoder.encode(newPassword);

        user.changePassword(encoded);
    }

}

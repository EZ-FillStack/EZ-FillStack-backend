package com.ezwell.backend.domain.auth;

import com.ezwell.backend.domain.auth.dto.AuthResponse;
import com.ezwell.backend.domain.auth.dto.LoginRequest;
import com.ezwell.backend.domain.auth.dto.SignupRequest;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor // 생성자 주입을 롬복으로 깔끔하게 처리
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public void signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalStateException("EMAIL_ALREADY_EXISTS");
        }

        String hash = passwordEncoder.encode(req.password());

        // 빌더 패턴을 사용해 수정된 User 엔티티 구조에 맞게 저장
        User user = User.builder()
          .username(req.username()) // 일단 이메일을 username으로 사용 (기획에 따라 변경 가능)
          .email(req.email())
          .password(hash)
          .nickname(req.nickname())
          .phone(req.phone())
          .provider("local")
          .build();

        userRepository.save(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
          .orElseThrow(() -> new IllegalStateException("INVALID_CREDENTIALS"));

        // getPasswordHash -> getPassword 로 변경된 필드명 반영
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new IllegalStateException("INVALID_CREDENTIALS");
        }

        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole().name());
    }

    // 비밀번호 찾기
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);

        return token;
    }
    // 비밀번호 재설정
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_TOKEN"));

        user.changePassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // 재사용 방지
    }
}
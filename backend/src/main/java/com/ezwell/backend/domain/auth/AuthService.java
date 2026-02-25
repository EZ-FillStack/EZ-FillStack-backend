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

    @Transactional
    public void signup(SignupRequest req) {
        //email 여부 확인
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalStateException("EMAIL_ALREADY_EXISTS");
        }
        String hash = passwordEncoder.encode(req.password()); //비밀번호 암호화

        // 생성자에서 자동으로 Role.USER 들어가게 변경됨
        User user = new User(req.email(), hash);
        userRepository.save(new User(req.email(), hash));
    }

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
        return new AuthResponse(token);
    }
}

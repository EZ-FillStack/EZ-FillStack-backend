package com.ezwell.backend.domain.user;

import com.ezwell.backend.domain.user.dto.PasswordUpdateRequest;
import com.ezwell.backend.domain.user.dto.UserResponse;
import com.ezwell.backend.domain.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 정보 부분 수정
    public void updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        user.updateProfile(
                request.nickname(),
                request.phone(),
                request.profileImageUrl()
        );
    }

    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        return UserResponse.from(user);
    }

    // 로그인 상태에서 비밀번호 변경
    public void changePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_PASSWORD");
        }

        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }
}
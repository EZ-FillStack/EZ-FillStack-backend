package com.ezwell.backend.domain.user;

import com.ezwell.backend.domain.user.dto.PasswordUpdateRequest;
import com.ezwell.backend.domain.user.dto.UserResponse;
import com.ezwell.backend.domain.user.dto.UserUpdateRequest;
import com.ezwell.backend.global.infra.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    
    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

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

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("INVALID_PASSWORD");
        }

        user.changePassword(passwordEncoder.encode(request.newPassword()));
    }

    // 프로필 이미지 업로드
    public String updateProfileImage(Long userId, MultipartFile file) {  // 수정: 오타 제거
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
 
        // 파일 검증
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
 
        String contentType = file.getContentType();
        
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
        
        //  사이즈 제한 3MB
        if (file.getSize() > 3 * 1024 * 1024) {
            throw new IllegalArgumentException("프로필 이미지는 3MB 이하여야 합니다.");
        }
 
        // 기존 이미지 삭제
        if (user.getProfileImageUrl() != null) {
            try {
                String oldFileName = extractFileNameFromUrl(user.getProfileImageUrl());
                s3Service.deleteFile(oldFileName);
                log.info("[프로필 이미지 삭제] userId:{}, 파일명:{}", userId, oldFileName);
            } catch (Exception e) {
                log.warn("[프로필 이미지 삭제 실패] userId:{}, 사유:{}", userId, e.getMessage());
            }
        }
 
        // 새 이미지 업로드
        String newImageUrl = s3Service.uploadFile(file, "profiles");
        log.info("[프로필 이미지 업로드] userId:{}, URL길이:{}", userId, newImageUrl.length());
 
        // DB 업데이트
        user.updateProfileImage(newImageUrl);
 
        return newImageUrl;
    }
 
    // R2 URL에서 S3 key 추출
    private String extractFileNameFromUrl(String url) {
        String marker = bucketName + "/";
        int idx = url.indexOf(marker);
        if (idx != -1) {
            return url.substring(idx + marker.length());
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
          .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
        user.softDelete(); // 상태를 DELETED로 변경
    }
}
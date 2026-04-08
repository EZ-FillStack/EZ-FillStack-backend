package com.ezwell.backend.domain.user;

import com.ezwell.backend.domain.user.dto.PasswordUpdateRequest;
import com.ezwell.backend.domain.user.dto.UserResponse;
import com.ezwell.backend.domain.user.dto.UserUpdateRequest;
import com.ezwell.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원 정보 수정
    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateRequest request
    ) {
        userService.updateUser(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUserId()));
    }

    // 로그인 상태에서 비밀번호 변경
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PasswordUpdateRequest request
    ) {
        userService.changePassword(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }
    
    // 프로필 이미지 수정
    @PatchMapping("/me/profile-image")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file) {
 
        String imageUrl = userService.updateProfileImage(userDetails.getUserId(), file);
 
        return ResponseEntity.ok(Map.of("profileImageUrl", imageUrl));
    }
}
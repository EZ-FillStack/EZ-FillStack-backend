package com.ezwell.backend.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    private String password; // 소셜 유저는 null 허용을 위해 nullable=false 제거

    private String resetToken;  // 비밀번호 재설정용 토큰

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String provider = "local"; // 기본값 local

    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(length = 500)
    private String profileImageUrl;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime deletedAt;

    @Builder
    public User(String username, String password, String nickname, String email, String phone,
                String provider, String providerId, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.provider = (provider != null) ? provider : "local";
        this.providerId = providerId;
        this.role = (role != null) ? role : Role.ROLE_USER;
    }

    // 비즈니스 로직 메서드들
    public void changeRole(Role role) {
        this.role = role;
    }

    public void updateProfile(String nickname, String phone, String profileImageUrl) {
        if (nickname != null) this.nickname = nickname;
        if (phone != null) this.phone = phone;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.status = "DELETED";
        this.deletedAt = LocalDateTime.now();
    }

    // 비밀번호 찾기용 토큰 저장 (forgotPassword에서 사용)
    public void setResetToken(String token) {
        this.resetToken = token;
    }

    // 비밀번호 재설정 (resetPassword에서 사용)
    public void changePassword(String newPasswordHash) {
        this.password = newPasswordHash; // 필드명이 password로 바뀌었으므로 확인!
        this.resetToken = null; // 사용 후 토큰 초기화
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public String getPasswordHash() {
        return this.password;
    }

	public void updateProfileImage(String newImageUrl) {
		this.profileImageUrl = newImageUrl;
	}
    
}
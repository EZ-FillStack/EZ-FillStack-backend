package com.ezwell.backend.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 (로그인 ID)
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String passwordHash;

    // 비밀번호 재설정 토큰
    @Column(length = 100)
    private String resetToken;

    // 권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 전화번호
    @Column(nullable = false)
    private String phone;

    // 프로필 이미지
    @Column(length = 500)
    private String profileImageUrl;

    // 기본 생성자
    public User(String email, String passwordHash, String nickname, String phone) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.phone = phone;
        this.role = Role.USER;
    }

    // 관리자/권한 지정용 생성자 - 관리자 권한 기본 나누는 이유 물어보기
    public User(String email, String passwordHash, Role role, String nickname, String phone) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nickname = nickname;
        this.phone = phone;
    }

    // 권한 변경
    public void changeRole(Role role) {
        this.role = role;
    }

    // 비밀번호 찾기용 토큰 저장
    public void setResetToken(String token) {
        this.resetToken = token;
    }

    // 비밀번호 재설정
    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        this.resetToken = null; // 사용 후 제거
    }

    // 로그인 상태에서 비밀번호 변경
    public void updatePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    // 부분 수정: null 아닌 필드만 변경
    public void updateProfile(String nickname, String phone, String profileImageUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

}
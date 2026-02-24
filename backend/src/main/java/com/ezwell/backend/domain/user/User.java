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

    // 로그인 ID (이메일)
    @Column(nullable = false, unique = true)
    private String email;

    // 암호화된 비밀번호
    @Column(nullable = false)
    private String passwordHash;

    // String role 필드 제거
    // private String role = "USER";

    // Role enum 타입으로 통일 변경 2/24
    @Enumerated(EnumType.STRING) // enum을 문자열로 DB 저장
    @Column(nullable = false)
    private Role role;

    // 기본 생성 시 USER 권한 자동 부여 되게 변경 2/24
    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = Role.USER; // 기본값 설정
    }

    // 관리자 생성용 생성자 추가 2/24
    public User(String email, String passwordHash, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // 권한 변경 메서드 추가 2/24
    public void changeRole(Role role) {
        this.role = role;
    }
}
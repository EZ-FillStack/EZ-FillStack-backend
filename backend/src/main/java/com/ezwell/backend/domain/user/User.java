package com.ezwell.backend.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ezwell.backend.domain.user.Role;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    // 기본키 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 ID (이메일)
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호 (암호화된 값)
    @Column(nullable = false)
    private String passwordHash;

    // 사용자 권한 (USER, ADMIN 등)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;


    // 생성자 (기본 정보로 생성 시 USER 권한 부여
    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = Role.USER;
    }

    // 관리자 생성 등 필요 시 사용할 전체 생성자
    public User(String email, String passwordHash, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}

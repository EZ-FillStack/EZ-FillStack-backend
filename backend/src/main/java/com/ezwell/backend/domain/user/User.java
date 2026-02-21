package com.ezwell.backend.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(nullable = false)
    private String role = "USER";

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
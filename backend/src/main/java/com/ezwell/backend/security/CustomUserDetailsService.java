package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. DB에서 이메일로 유저를 찾음
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        // 2. Spring Security의 User 객체로 변환하여 반환
        // Role은 "ROLE_USER" 또는 "ROLE_ADMIN" 형태의 문자열로 권한을 부여
        return new org.springframework.security.core.userdetails.User(
          user.getEmail(),
          user.getPasswordHash(),
          Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()))
        );
    }
}
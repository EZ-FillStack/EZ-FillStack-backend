package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    // ⚠️ 실서비스에서는 환경변수로 빼기 (최소 32바이트 이상)
    private static final String SECRET = "studyspot-secret-studyspot-secret-32bytes!";
    private static final long ACCESS_TOKEN_MS = 1000L * 60 * 60 * 2; // 2h

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 1. 토큰 생성 (userId, email, Role 타입을 받도록 개선)
    public String createAccessToken(Long userId, String email, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ACCESS_TOKEN_MS);

        return Jwts.builder()
                .setSubject(email)
                .claim("uid", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Spring Security 인증 정보 조회 (필터에서 사용)
    public Authentication getAuthentication(String token) {
        // 토큰에서 이메일을 추출하여 유저 정보를 로드함
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 3. 토큰에서 이메일 추출
    public String getUserEmail(String token) {
        return parse(token).getBody().getSubject();
    }

    // 4. 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
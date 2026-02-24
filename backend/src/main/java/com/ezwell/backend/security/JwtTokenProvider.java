package com.ezwell.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import com.ezwell.backend.domain.user.Role;

@Component
public class JwtTokenProvider {

    // ⚠️ 실서비스에서는 환경변수로 빼기 (최소 32바이트 이상)
    private static final String SECRET = "studyspot-secret-studyspot-secret-32bytes!";
    private static final long ACCESS_TOKEN_MS = 1000L * 60 * 60 * 2; // 2h

    private final Key key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long id, String email, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ACCESS_TOKEN_MS);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", id)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
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
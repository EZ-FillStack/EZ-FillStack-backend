package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String SECRET = "ezfillstack-jwt-secret-key-32bytes-long!";
    private static final long ACCESS_TOKEN_MS = 1000L * 60 * 60 * 2;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 토큰 생성
    public String createAccessToken(Long userId, String email, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ACCESS_TOKEN_MS);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ⭐ 핵심: DB 조회 없이 인증 객체 생성
    public Authentication getAuthentication(String token) {

        Claims claims = parse(token).getBody();

        Long userId = claims.get("userId", Integer.class).longValue();
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        CustomUserDetails userDetails = new CustomUserDetails(
                userId,
                email,
                Role.valueOf(role)
        );

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    public String resolveToken(jakarta.servlet.http.HttpServletRequest request) {
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
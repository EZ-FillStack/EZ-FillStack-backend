package com.ezwell.backend.domain.user.dto;

import com.ezwell.backend.domain.user.User;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        String phone,
        String profileImageUrl,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhone(),
                user.getProfileImageUrl(),
                user.getRole().name()
        );
    }
}
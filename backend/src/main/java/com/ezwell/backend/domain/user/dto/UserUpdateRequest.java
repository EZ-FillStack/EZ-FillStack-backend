package com.ezwell.backend.domain.user;

public record UserUpdateRequest(
        String nickname,
        String phone,
        String profileImageUrl
) {}
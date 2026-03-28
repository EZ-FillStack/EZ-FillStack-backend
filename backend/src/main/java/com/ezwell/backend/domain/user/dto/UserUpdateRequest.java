package com.ezwell.backend.domain.user.dto;

public record UserUpdateRequest(
        String nickname,
        String phone,
        String profileImageUrl
) {}
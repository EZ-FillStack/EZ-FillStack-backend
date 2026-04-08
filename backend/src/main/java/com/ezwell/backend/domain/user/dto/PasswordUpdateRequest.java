package com.ezwell.backend.domain.user.dto;

public record PasswordUpdateRequest(
        String currentPassword,
        String newPassword
) {}
package com.ezwell.backend.domain.auth.dto;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {}
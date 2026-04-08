package com.ezwell.backend.domain.admin.dto;

import com.ezwell.backend.domain.user.Role;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
        @NotNull Role role
) {}
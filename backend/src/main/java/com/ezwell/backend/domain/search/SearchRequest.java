package com.ezwell.backend.domain.search;

import jakarta.validation.constraints.Size;

public record SearchRequest(
        @Size(max = 50, message = "검색어는 50자 이하여야 합니다.")
        String keyword
) {
    public String getKeyword() {
        return keyword == null ? null : keyword.trim();
    }
}
package com.ezwell.backend.domain.search;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequest {
	
	@Size(max = 50, message="검색어는 50자 이하여야 합니다. ")
	private String keyword;
	
	// 검증 통과 후 호출
	public String getKeyword() {
		return (keyword == null) ? null : keyword.trim();
	}
}
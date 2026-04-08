package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezwell.backend.domain.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search") // 경로 구조화
public class SearchController {
	
	private final SearchService searchService;
	
	@GetMapping
    public ResponseEntity<Page<Event>> searchEvents(
            @ModelAttribute @Validated SearchRequest request,
            @PageableDefault(size=10) Pageable pageable) {
		
		// 검증 완료 된 keyword 사용
		Page<Event> result  = searchService.searchPost(request.getKeyword(), pageable);
		
		// 보안 로깅
        log.info("[Search] 검색어: {}, 결과 수: {} ",
        		request.getKeyword() == null ? 0 : request.getKeyword().length(),
        		result.getTotalElements());
        
        return ResponseEntity.ok(result); // 데이터를 JSON으로 반환
	}
}
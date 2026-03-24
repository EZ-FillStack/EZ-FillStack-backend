package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
		
        // 검색어 정제(앞뒤 공백 null)
		String searchKeyword = (keyword != null) ? keyword.trim():"";
		
        log.info("[Search] 검색어: {}", searchKeyword);
        Page<Event> result = searchService.searchPost(searchKeyword, pageable);
        
        // 검색결과 없을 때
        if(result.isEmpty()) {
        	log.info("검색 결과가 없습니다.");
        }
        return ResponseEntity.ok(result); // 데이터를 JSON으로 반환
    }
}

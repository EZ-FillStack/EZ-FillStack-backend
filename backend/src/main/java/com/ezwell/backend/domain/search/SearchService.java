package com.ezwell.backend.domain.search;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.ezwell.backend.domain.event.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 50;
	private static final int MAX_KEYWORD_LENGTH = 50;
	
	private final SearchRepository searchRepository;
	
	// 검색 결과 null 처리
	@Cacheable(
			value = "searchCache",
			key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize",
			unless= "#result.totalElements == 0")
	
	@Transactional(readOnly = true)
	public Page<Event> searchPost(String keyword,Pageable pageable){
		
		// XSS 보안
		String sanitized = sanitize(keyword);
		
	    log.info("[SearchPost] 검색 키워드 : [{}]", sanitized == null? 0 : sanitized.length());
	    
	    int pageSize = Math.min(pageable.getPageSize()> 0 ? pageable.getPageSize() : DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);
	    
	    // 페이지번호를 0부터 시작하도록 시작
	    int pageNumber = (pageable.getPageNumber()<=0)?0:(pageable.getPageNumber()-1);
	    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
	    
	    // 키워드 분기 처리
        if (sanitized == null || sanitized.isBlank()) {
            return searchRepository.findAll(pageRequest); 
        }
        return searchRepository.findByTitleContainingOrDescriptionContaining(sanitized, pageRequest);
    }
	
    /**
     * XSS 및 SQL Injection 방어를 위한 입력값 정제
     * - HTML 이스케이프 (XSS 방어)
     * - 길이 제한
     * - SQL LIKE 특수문자 이스케이프 (%, _, \)
     */
	private String sanitize (String input) {
		if(input == null) return null;
		
		String trimmed = input.trim();
		if(trimmed.isEmpty()) return null;
		if(trimmed.length() > MAX_KEYWORD_LENGTH) {
			trimmed = trimmed.substring(0, MAX_KEYWORD_LENGTH);
		}
		
		// XSS 방어
		String xssEscaped = HtmlUtils.htmlEscape(trimmed);
		return escapeSqlLike(xssEscaped);
	}
	
    /**
     * JPA LIKE 쿼리에서 사용되는 특수문자 이스케이프
     * - % : 임의의 문자열 매칭 → \%
     * - _ : 임의의 단일 문자 매칭 → \_
     * - \ : 이스케이프 문자 자체 → \\
     */
	private String escapeSqlLike(String input) {
		return input
				.replace("\\", "\\\\")
				.replace("%", "\\%")
				.replace("_", "\\_");
	}
}
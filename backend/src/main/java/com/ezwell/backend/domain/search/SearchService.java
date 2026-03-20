package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.event.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
	
	private final SearchRepository searchRepository;
	
	@Transactional(readOnly = true)
	public Page<Event> searchPost(String keyword,Pageable pageable){
	    log.info("[SearchPost] 검색 키워드 : [{}]",keyword);
	    
	    // 페이지번호를 0부터 시작하도록 시작
	    int page = (pageable.getPageNumber()<=0)?0:(pageable.getPageNumber()-1);
	    PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
	    
	    // 키워드 분기 처리
        if (keyword == null || keyword.trim().isEmpty()) {
            return searchRepository.findAll(pageRequest); 
        }
        return searchRepository.findByTitleContaining(keyword, pageRequest);
    }
}
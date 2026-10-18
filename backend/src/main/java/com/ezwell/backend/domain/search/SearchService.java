package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.event.dto.EventResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
	
	private static final int MAX_PAGE_SIZE = 50;
	private static final int MAX_KEYWORD_LENGTH = 50;
	
	private final SearchRepository searchRepository;
	
	// 조회 전용 트랜잭션
	@Transactional(readOnly = true)
    public Page<EventResponse> searchPost(
            String keyword,
            Pageable pageable
    ) {
        // 앞뒤 공백 제거 및 길이 제한
        String normalized = normalizeKeyword(keyword);
        
        int pageSize = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);

        // 요청의 sort 값 대신 id 내림차순으로 정렬을 고정
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        // 검색어 원문은 남기지 않고 길이만 기록
        log.info(
                "[SearchPost] 검색어 길이: {}",
                normalized == null ? 0 : normalized.length()
        );

        // 검색어가 없으면 전체 목록을 페이지 단위로 조회
        if (normalized == null) {
            return searchRepository.findAll(pageRequest)
                    .map(EventResponse::from);
        }

        String escaped = escapeLike(normalized);

        // 제목 또는 설명에 검색어가 포함된 이벤트 조회
        return searchRepository
                .findByTitleContainingOrDescriptionContaining(
                        escaped, pageRequest
                )
                .map(EventResponse::from);
    }

    private String normalizeKeyword(String input) {
        // 검색어 파라미터가 없는 경우
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        // 다른 코드에서 서비스를 직접 호출할 경우에도 길이 제한
        if (trimmed.length() > MAX_KEYWORD_LENGTH) {
            trimmed = trimmed.substring(0, MAX_KEYWORD_LENGTH);
        }
        return trimmed;
    }

    private String escapeLike(String input) {
        /* Repository 쿼리의 ESCAPE '!'와 동일한 문자를 사용
           ! → !! : 이스케이프 문자 자체를 검색
           % → !% : 임의의 문자열이 아닌 실제 %를 검색
           _ → !_ : 임의의 한 글자가 아닌 실제 _를 검색

           !를 먼저 변환해야 뒤에서 추가한 !가 다시 변환되지 않음
           이 처리는 LIKE 패턴 제어용이며,
           쿼리 값 전달은 Repository의 바인딩 파라미터가 담당 */
        return input
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }
}
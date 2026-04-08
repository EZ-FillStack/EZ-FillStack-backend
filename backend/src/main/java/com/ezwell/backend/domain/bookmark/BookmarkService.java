package com.ezwell.backend.domain.bookmark;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.bookmark.dto.BookmarkListResponse;
import com.ezwell.backend.domain.bookmark.dto.BookmarkResponse;
import com.ezwell.backend.domain.bookmark.exception.BookmarkedException;
import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final EventRepository eventRepository;
	private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new BookmarkedException("로그인 후 이용 가능합니다."));
    }
	
	// 북마크 등록
	@Transactional
	public BookmarkResponse addBookmark(Long eventId) {
        User user = getCurrentUser();
		
		Event event = eventRepository.findById(eventId)
				.orElseThrow(()-> new EventNotFoundException());

		// 중복 방지
	    if(bookmarkRepository.findByUserAndEvent(user, event) != null) {
	    	throw new BookmarkedException("이미 북마크된 행사입니다.");
	    }
	    
	    Bookmark bookmark = Bookmark.createBookmark(user, event);
	    bookmarkRepository.save(bookmark);
	    
	    // 북마크 카운트 증가
	    event.increaseBookmarkCount();
	    
	    return BookmarkResponse.createBookmarkDto("북마크 저장 완료", bookmark);
	} 
	
	
	// 북마크 삭제
	@Transactional
	public void deleteBookmark(Long eventId) {
        User user = getCurrentUser();
		
		Event event = eventRepository.findById(eventId)
				.orElseThrow(()-> new EventNotFoundException());

	    Bookmark bookmark = bookmarkRepository.findByUserAndEvent(user, event);
	    
	    // 삭제 전 유효성 확인
	    if(bookmark == null) {
	    	throw new BookmarkedException("북마크 내역을 찾을 수 없습니다.");
	    }
	    
	    // 북마크 카운트 감소
	    event.decreaseBookmarkCount();
	    
	    bookmarkRepository.delete(bookmark);
	    
	}
	
	
	// 마이페이지 북마크
	@Transactional(readOnly = true)
	public List<BookmarkListResponse> getMyBookmarks(){
        User user = getCurrentUser();
		
		return bookmarkRepository.findAllByUserOrderByCreateDateDesc(user)
				.stream()
				.map(BookmarkListResponse::from)
				.collect(Collectors.toList());
	}
}

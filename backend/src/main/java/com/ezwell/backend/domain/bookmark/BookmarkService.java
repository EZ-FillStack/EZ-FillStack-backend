package com.ezwell.backend.domain.bookmark;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.bookmark.dto.BookmarkListResponse;
import com.ezwell.backend.domain.bookmark.dto.BookmarkResponse;
import com.ezwell.backend.domain.bookmark.exception.AlreadyBookmarkedException;
import com.ezwell.backend.domain.bookmark.exception.BookmarkNotFoundException;
import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final EventRepository eventRepository;
	private final BookmarkRepository bookmarkRepository;
	
	// 유저 확인
	private User validateUser (HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) throw new BookmarkNotFoundException("로그인 후 이용 가능합니다.");
		return user;
	}
	
	// 북마크 등록
	@Transactional
	public BookmarkResponse addBookmark(Long eventId, HttpSession session) {
		User user = validateUser(session);
		
		Event event = eventRepository.findById(eventId)
				.orElseThrow(()-> new EventNotFoundException());

		// 중복 방지
	    if(bookmarkRepository.findByUserAndEvent(user, event) != null) {
	    	throw new AlreadyBookmarkedException("이미 북마크된 행사입니다.");
	    }
	    
	    Bookmark bookmark = Bookmark.createBookmark(user, event);
	    bookmarkRepository.save(bookmark);
	    
	    // 북마크 카운트 증가
	    event.increaseBookmarkCount();
	    
	    return BookmarkResponse.createBookmarkDto("북마크 저장 완료", bookmark);
	} 
	
	
	// 북마크 삭제
	@Transactional
	public void deleteBookmark(Long eventId, HttpSession session) {
		User user = validateUser(session);
		
		Event event = eventRepository.findById(eventId)
				.orElseThrow(()-> new EventNotFoundException());

	    Bookmark bookmark = bookmarkRepository.findByUserAndEvent(user, event);
	    
	    if(bookmark == null) {
	    	throw new BookmarkNotFoundException("북마크 내역을 찾을 수 없습니다.");
	    }
	    
	    // 북마크 카운트 감소
	    event.decreaseBookmarkCount();
	    
	    bookmarkRepository.delete(bookmark);
	    
	}
	
	
	// 마이페이지 북마크
	@Transactional(readOnly = true)
	public List<BookmarkListResponse> getMyBookmarks(HttpSession session){
		User user = validateUser(session);
		
		return bookmarkRepository.findAllByUserOrderByCreateDateDesc(user)
				.stream()
				.map(BookmarkListResponse::from)
				.collect(Collectors.toList());
	}
}

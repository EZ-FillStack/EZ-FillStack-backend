package com.ezwell.backend.domain.bookmark;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezwell.backend.domain.bookmark.dto.BookmarkListResponse;
import com.ezwell.backend.domain.bookmark.dto.BookmarkResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;
	
	// 북마크
	@PostMapping("/events/{eventId}/bookmark")
	public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable("eventId") Long eventId){
		BookmarkResponse response = bookmarkService.addBookmark(eventId);
		return ResponseEntity.ok(response);
	}
	
	// 북마크 삭제
	@DeleteMapping("/events/{eventId}/bookmark")
	public ResponseEntity<Void> deleteBoomark(@PathVariable("eventId") Long eventId){
		bookmarkService.deleteBookmark(eventId);
		return ResponseEntity.noContent().build();
	}
	
	// 마이페이지 북마크
	@GetMapping("/me/bookmarks")
	public ResponseEntity<List<BookmarkListResponse>> getMyBookmarks(){
		List<BookmarkListResponse> bookmarks = bookmarkService.getMyBookmarks();
		return ResponseEntity.ok(bookmarks);
	}
}

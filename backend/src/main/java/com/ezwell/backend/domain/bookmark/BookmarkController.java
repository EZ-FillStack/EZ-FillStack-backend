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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;
	
	// 북마크
	@PostMapping("/events/{eventId}/bookmark")
	public ResponseEntity<BookmarkResponse> addBookmark(@PathVariable Long eventId, HttpServletRequest request){
		BookmarkResponse response = bookmarkService.addBookmark(eventId, request.getSession());
		return ResponseEntity.ok(response);
	}
	
	// 북마크 삭제
	@DeleteMapping("/events/{eventId}/bookmark")
	public ResponseEntity<Void> deleteBoomark(@PathVariable Long eventId, HttpServletRequest request){
		bookmarkService.deleteBookmark(eventId, request.getSession());
		return ResponseEntity.noContent().build();
	}
	
	// 마이페이지 북마크
	@GetMapping("/my/bookmarks")
	public ResponseEntity<List<BookmarkListResponse>> getMyBookmarks(HttpServletRequest request){
		List<BookmarkListResponse> bookmarks = bookmarkService.getMyBookmarks(request.getSession());
		return ResponseEntity.ok(bookmarks);
	}
}

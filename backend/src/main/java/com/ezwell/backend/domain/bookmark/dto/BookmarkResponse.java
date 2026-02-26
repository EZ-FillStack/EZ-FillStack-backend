package com.ezwell.backend.domain.bookmark.dto;

import com.ezwell.backend.domain.bookmark.Bookmark;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkResponse {
	private String message;
	private boolean status;
	
	public static BookmarkResponse createBookmarkDto(String message, Bookmark bookmark) {
		return new BookmarkResponse(
			message,
			bookmark.isStatus()
		);
	}
}

package com.ezwell.backend.domain.bookmark.dto;

import java.time.LocalDateTime;

import com.ezwell.backend.domain.bookmark.Bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkListResponse {
	private Long bookmarkId;
	private Long eventId;
	private String eventTitle;
	private LocalDateTime createDate;
	
	public static BookmarkListResponse from(Bookmark bookmark) {
		return new BookmarkListResponse(
			bookmark.getId(),
			bookmark.getEvent().getId(),
			bookmark.getEvent().getTitle(),
			bookmark.getCreateDate()
		);
	}
}

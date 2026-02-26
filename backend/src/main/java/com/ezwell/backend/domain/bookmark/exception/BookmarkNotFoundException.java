package com.ezwell.backend.domain.bookmark.exception;

public class BookmarkNotFoundException extends RuntimeException {
	public BookmarkNotFoundException(String message) {
	    super(message);
	}
}

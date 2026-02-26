package com.ezwell.backend.domain.bookmark;


import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookmarks",
	uniqueConstraints = {@UniqueConstraint(columnNames={"user_id","event_id"})})
public class Bookmark {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 북마크하는 유저
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	// 북마크되는 이벤트
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="event_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Event event;
	
	// 북마크하지 않은 이벤트는 기본 false값
	@Column(nullable = false)
	private boolean status;
	
	// 북마크 생성날짜 기준 내림차순
	@Column
	private LocalDateTime createDate;
	
	// 북마크
    public static Bookmark createBookmark(User user, Event event) {
        return new Bookmark(
                null,
                user,
                event,
                true,
                LocalDateTime.now()
        );
    }
	
	// 북마크 취소
	public void deletebookmark(Event event) {
		this.status = false;
	}
}

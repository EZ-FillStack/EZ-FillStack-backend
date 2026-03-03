package com.ezwell.backend.domain.bookmark;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.user.User;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
	
	// 북마크 조회할 때 event 정보도 같이 로딩
	@EntityGraph(attributePaths = {"event"})
	Page<Bookmark> findAllByUser(User user, Pageable pageable);
	
	Bookmark findByUserAndEvent(User user, Event event);
	void deleteByUserAndEvent(User user, Event event);

    // 특정 유저별 북마크 리스트 생성일 내림차순 조회
    List<Bookmark> findAllByUserOrderByCreateDateDesc(User user);
}
package com.ezwell.backend.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 기본 조회
    Optional<Event> findById(Long id);

    // 동시성 제어
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);

    // 예정된 이벤트 조회
    List<Event> findByEventStartDateTimeAfter(LocalDateTime now);

    // 카테고리별 이벤트 조회
    List<Event> findByCategoryId(Long categoryId);

    // 인기순 이벤트 조회
    List<Event> findAllByOrderByBookmarkCountDesc();
}
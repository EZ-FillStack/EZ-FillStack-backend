package com.ezwell.backend.domain.event;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 상태별 조회
    List<Event> findByStatus(EventStatus status);

    // 카테고리별 조회
    List<Event> findByCategory_Id(Long categoryId);

    // 상태 + 카테고리
    List<Event> findByStatusAndCategory_Id(EventStatus status, Long categoryId);

    // 최신순 조회
    List<Event> findAllByOrderByCreatedAtDesc();

    // 상태 + 최신순
    List<Event> findByStatusOrderByCreatedAtDesc(EventStatus status);

    // 비관적 락 (동시성 제어 핵심)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);
}
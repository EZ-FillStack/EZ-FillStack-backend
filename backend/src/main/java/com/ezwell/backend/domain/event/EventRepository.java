package com.ezwell.backend.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 기본 조회
    Optional<Event> findById(Long id);

    // 최신 이벤트 조회 (선택 기능)
    List<Event> findAllByOrderByCreatedAtDesc();

    // 동시성 제어 (핵심)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);
}
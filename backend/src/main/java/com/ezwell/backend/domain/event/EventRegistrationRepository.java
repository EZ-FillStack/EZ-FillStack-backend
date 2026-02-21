package com.ezwell.backend.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    List<EventRegistration> findByUserId(Long userId);
    void deleteByEventIdAndUserId(Long eventId, Long userId);
}
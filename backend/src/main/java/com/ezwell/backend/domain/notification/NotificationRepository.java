package com.ezwell.backend.domain.notification;

import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @EntityGraph(attributePaths = {"event"})
    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);

    Optional<Notification> findByUserAndEvent(User user, Event event);

    boolean existsByUserAndEvent(User user, Event event);

    List<Notification> findAllByIsSentFalse();

    @EntityGraph(attributePaths = {"user", "event"})
    List<Notification> findAllByIsSentFalseOrderByCreatedAtAsc();
}
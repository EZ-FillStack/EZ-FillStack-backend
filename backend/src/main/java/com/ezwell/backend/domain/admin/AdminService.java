package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.event.EventService;
import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import com.ezwell.backend.domain.user.Role;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final EventService eventService;
    private final UserRepository userRepository;

    public EventResponse createEvent(EventCreateRequest request) {
        return eventService.createEvent(request);
    }

    public EventResponse updateEvent(Long eventId, EventUpdateRequest request) {
        return eventService.updateEvent(eventId, request);
    }

    public void deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
    }

    public void changeUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
        user.changeRole(role);
    }
}
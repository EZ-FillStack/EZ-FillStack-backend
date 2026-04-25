package com.ezwell.backend.domain.admin;

import com.ezwell.backend.domain.application.dto.ApplicationResponse;
import com.ezwell.backend.domain.event.EventService;
import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.dto.EventUpdateRequest;
import com.ezwell.backend.domain.user.Role;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.admin.dto.ApplicationStatusRequest;
import com.ezwell.backend.domain.application.Application;
import com.ezwell.backend.domain.application.ApplicationRepository;

import com.ezwell.backend.external.ExternalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ExternalEventService externalEventService;

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

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));
        userRepository.delete(user);
    }

    // 신청 전체 조회
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    //  특정 이벤트의 신청 목록 조회
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByEvent(Long eventId) {
        return applicationRepository.findByEventId(eventId).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    //  신청 상태 변경
    public void updateApplicationStatus(Long applicationId, ApplicationStatusRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));

        application.updateStatus(request.getStatus());
    }

    // 신청 강제 삭제
    public void deleteApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("APPLICATION_NOT_FOUND"));
        applicationRepository.delete(application);
    }

    public void syncKopisEvents(String stdate, String eddate, int cpage, int rows) {
        String xmlData = externalEventService.getPerformances(stdate, eddate, cpage, rows);

        System.out.println("동기화(Sync) 호출 성공! 받아온 데이터:\n" + xmlData);
    }
}
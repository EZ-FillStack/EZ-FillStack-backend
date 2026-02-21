package com.ezwell.backend.domain.event;

import com.ezwell.backend.domain.category.Category;
import com.ezwell.backend.domain.category.CategoryRepository;
import com.ezwell.backend.domain.event.dto.EventCreateRequest;
import com.ezwell.backend.domain.event.dto.EventResponse;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserRepository userRepository;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(EventResponse::from).toList();
    }

    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));

        Event event = new Event(request.title(), request.capacity(), category);
        event = eventRepository.save(event);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse applyToEvent(Long eventId) {
        Long userId = getLoginUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("USER_NOT_FOUND"));

        //repository PESSIMISTIC_WRITE를 통해 DB에서 해당 row 락 걸음
        // ㄴ-> 한 명 신청 처리 끝날 때까지 다른 트랜잭션 대기
        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(EventNotFoundException::new);

        //이벤트 신청 가능 여부 검사
        event.validateApplicable(LocalDateTime.now());

        if (eventRegistrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("ALREADY_APPLIED");
        }


        eventRegistrationRepository.save(new EventRegistration(event, user));
        event.increaseParticipants();

        return EventResponse.from(event);
    }

    //정원 감소 처리
    @Transactional
    public void cancelApply(Long eventId) {
        Long userId = getLoginUserId();

        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (!eventRegistrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalStateException("NOT_APPLIED");
        }

        eventRegistrationRepository.deleteByEventIdAndUserId(eventId, userId);
        event.decreaseParticipants();
    }

    //신청 테이블 기준 조회
    public List<EventResponse> myEvents() {
        Long userId = getLoginUserId();
        return eventRegistrationRepository.findByUserId(userId)
                .stream()
                .map(r -> EventResponse.from(r.getEvent()))
                .toList();
    }

    //현재 로그인 사용자 가져오기
    private Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new IllegalStateException("UNAUTHORIZED");
        }
        return cud.getUserId();
    }
}
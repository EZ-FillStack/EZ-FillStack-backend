package com.ezwell.backend.domain.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.application.dto.MyApplicationResponse;
import com.ezwell.backend.domain.application.exception.ApplicationEventException;
import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.review.ReviewRepository;
import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    Application application;

    // JWT 기반 유저 추출
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new ApplicationEventException("로그인 후 이용 가능합니다."));
    }

	// 이벤트 신청
    @Transactional
    public void applyEvent(Long eventId) {
        User user = getCurrentUser();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
        
        event.validateApplicable(LocalDateTime.now());

        // 중복 신청 체크
        if (applicationRepository.existsByUserAndEvent(user, event)) {
            throw new ApplicationEventException("이미 신청한 행사입니다.");
        }
        	
		// 인원 제한
		if(event.getCurrentParticipants() >= event.getCapacity()) {
			application = Application.builder()
					.user(user)
					.event(event)
					.status(ApplicationStatus.REJECTED)
					.build();
			applicationRepository.save(application);
			return;
		}
		
		// 신청 저장
		Application application = Application.builder()
				.user(user)
				.event(event)
				.build();
		
		applicationRepository.save(application);
		event.increaseParticipants();
	}
	
	// 이벤트 취소
    @Transactional
	public void cancelApplication(Long applicationId) {
        User user = getCurrentUser();
		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(()-> new ApplicationEventException("신청 내역을 찾을 수 없습니다."));
		
		// 본인 확인
	    if (!application.getUser().getId().equals(user.getId())) {
	        throw new ApplicationEventException("본인의 신청만 취소할 수 있습니다.");
	    }
		
		boolean wasApproved = application.getStatus() == ApplicationStatus.APPROVED;
		application.cancel();
		
		if(wasApproved) {
			application.getEvent().decreaseParticipants();
		}
	}
	
	// 신청 이벤트 목록
    @Transactional(readOnly = true)
	public List<MyApplicationResponse> getMyApplications(){
        User user = getCurrentUser();
        Long userId = user.getId();
        
        List<Application> applications = applicationRepository.findAllByUserAndStatusNotOrderByAppliedAtDesc(user, ApplicationStatus.CANCELED);
 
        List<Long> eventIds = applications.stream()
        		.map(a->a.getEvent().getId())
        		.toList();

        Set<Long> reviewedEventIds = eventIds.isEmpty()
            ? Set.of()
            : reviewRepository.findEventIdsByUserIdAndEventIdIn(userId, eventIds);
        
        return applications.stream()
        		.map(a -> MyApplicationResponse.of(
        				a, reviewedEventIds.contains(a.getEvent().getId())
        				))
        		.toList();
    }
    
}

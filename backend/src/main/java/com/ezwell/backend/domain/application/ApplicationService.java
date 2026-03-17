package com.ezwell.backend.domain.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.application.dto.MyApplicationResponse;
import com.ezwell.backend.domain.application.exception.ApplicationEventException;
import com.ezwell.backend.domain.event.Event;
import com.ezwell.backend.domain.event.EventRepository;
import com.ezwell.backend.domain.event.exception.CapacityExceededException;
import com.ezwell.backend.domain.event.exception.EventNotFoundException;
import com.ezwell.backend.domain.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final EventRepository eventRepository;
	
	// 유저 확인
	private User validateUser (HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) throw new ApplicationEventException("로그인 후 이용 가능합니다.");
		return user;
	}

	// 이벤트 신청
	@Transactional
	public void applyEvent(Long eventId, HttpSession session) {
		User user = validateUser(session);
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException());
		
		// 중복 신청 체크
		if(applicationRepository.existsByUserAndEvent(user, event)) { 
			throw new ApplicationEventException("이미 신청한 행사입니다.");
		}
		
		// 인원 제한
		if(event.getCurrentParticipants() >= event.getCapacity()) {
			throw new CapacityExceededException();
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
	public void cancelApplication(Long eventId, HttpSession session) {
		
		Application application = applicationRepository.findById(eventId)
				.orElseThrow(()-> new ApplicationEventException("신청 내역을 찾을 수 없습니다."));
		
		application.cancel();
		application.getEvent().decreaseParticipants();
	}
	
	// 신청 이벤트 목록
    @Transactional(readOnly = true)
	public List<MyApplicationResponse> getMyApplications(HttpSession session){
		User user = validateUser(session);
		return applicationRepository.findAllByUserOrderByAppliedAtDesc(user)
				.stream()
				.map(MyApplicationResponse::from)
				.collect(Collectors.toList());
	}
}

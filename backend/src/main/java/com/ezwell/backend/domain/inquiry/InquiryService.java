package com.ezwell.backend.domain.inquiry;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezwell.backend.domain.inquiry.dto.InquiryRequest;
import com.ezwell.backend.domain.inquiry.dto.InquiryResponse;
import com.ezwell.backend.domain.inquiry.exception.InquiryException;
import com.ezwell.backend.domain.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {
	private final InquiryRepository inquiryRepository;
	private final InquiryMailService inquiryMailService;
	
	
	private User validateUser(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) throw new InquiryException("로그인 후 이용 가능합니다.");
		return user;
	}
	

	/// 유저
	
	// 문의 등록
	@Transactional
	public void createInquiry(InquiryRequest dto, HttpSession session) {
		User user = validateUser(session);
		
		Inquiry inquiry = Inquiry.builder()
				.user(user)
				.title(dto.getTitle())
				.content(dto.getContent())
				.status(InquiryStatus.UNANSWERED)
				.createdAt(LocalDateTime.now())
				.build();
		
		inquiryRepository.save(inquiry);
	}
	
	// 문의 목록 조회
	@Transactional(readOnly=true)
	public List<InquiryResponse> getMyInquiries(HttpSession session){
		User user = validateUser(session);
		return inquiryRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
				.map(InquiryResponse::from)
				.collect(Collectors.toList());
	}
	
	// 문의 수정
	@Transactional
	public void updateInquiry(Long id, InquiryRequest dto, HttpSession session) {
		User user = validateUser(session);
		Inquiry inquiry = inquiryRepository.findById(id)
				.orElseThrow(()-> new InquiryException("문의를 찾을 수 없습니다."));
		// 본인 확인
		if(!inquiry.getUser().getId().equals(user.getId())) {
			throw new InquiryException("수정 권한이 없습니다.");
		}
		inquiry.updateInquiry(dto.getTitle(), dto.getContent());
	}
	
	// 문의 삭제
	@Transactional
	public void deleteInquiry(Long inquiryId, HttpSession session) {
		User user = validateUser(session);
		Inquiry inquiry = inquiryRepository.findById(inquiryId)
				.orElseThrow(()-> new InquiryException("문의를 찾을 수 없습니다."));
		// 본인 확인
		if(!inquiry.getUser().getId().equals(user.getId())) {
			throw new InquiryException("삭제 권한이 없습니다.");
		}
		inquiryRepository.delete(inquiry);
	}
	
	/// 관리자
	
	// 문의 목록 조회
	@Transactional(readOnly = true)
	public List<InquiryResponse> getAllInquiries(){
		return inquiryRepository.findAllByOrderByCreatedAtDesc()
				.stream()
				.map(InquiryResponse::from)
				.collect(Collectors.toList());
	}
	
	// 관리자 답변 등록 -> DB 저장 + 이메일 발송
	@Transactional
	public void answerInquiry(Long inquiryId, String replyContent) {
		Inquiry inquiry = inquiryRepository.findById(inquiryId)
				.orElseThrow(()-> new InquiryException("문의를 찾을 수 없습니다."));
		
		// 1. DB 답변 내용 저장 + 상태변경
		inquiry.answer(replyContent);
		
		// 2. 유저 이메일로 답변 전송
		String toEmail = inquiry.getUser().getEmail();
		inquiryMailService.sendReplyEmail(toEmail, inquiry.getTitle(), replyContent);
	}
}

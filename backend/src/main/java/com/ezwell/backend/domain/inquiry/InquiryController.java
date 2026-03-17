package com.ezwell.backend.domain.inquiry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ezwell.backend.domain.inquiry.dto.InquiryRequest;
import com.ezwell.backend.domain.inquiry.dto.InquiryResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;
	
	// 문의하기
	@PostMapping("/inquirie")
	public ResponseEntity<Void> create(@RequestBody InquiryRequest dto, HttpServletRequest request){
		inquiryService.createInquiry(dto, request.getSession());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 유저 문의 내역
	@GetMapping("/users/me/inquiries")
	public ResponseEntity<List<InquiryResponse>> getMyList(HttpServletRequest request){
		return ResponseEntity.ok(inquiryService.getMyInquiries(request.getSession()));
	}
	
	// 문의 수정
	@PatchMapping("/inquirie/{eventId}")
	public ResponseEntity<Void> updateInquiry(@PathVariable("eventId") Long id, @RequestBody InquiryRequest dto,
																		HttpServletRequest request) {
		inquiryService.updateInquiry(id, dto, request.getSession());
		return ResponseEntity.ok().build();
	}
	
	// 문의 삭제
	@DeleteMapping("/inquirie/{eventId}")
	public ResponseEntity<Void> deleteInquiry(@PathVariable("eventId") Long id, HttpServletRequest request){
		inquiryService.deleteInquiry(id, request.getSession());
		return ResponseEntity.noContent().build();
	}
	
	// 관리자 답변
	@PatchMapping("/admin/inquirie/{eventId}")
	public ResponseEntity<Void> answer(@PathVariable("eventId") Long id, @RequestBody InquiryRequest dto){
		inquiryService.answerInquiry(id, dto.getAnswerContent());
		return ResponseEntity.ok().build();
	}
}
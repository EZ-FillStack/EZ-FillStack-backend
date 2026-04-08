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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;
	
	
	/// 유저

	// 문의하기
	@PostMapping("/inquiry")
	public ResponseEntity<Void> create(@RequestBody InquiryRequest dto){
		inquiryService.createInquiry(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 문의 내역
	@GetMapping("/users/me/inquiries")
	public ResponseEntity<List<InquiryResponse>> getMyList(){
		return ResponseEntity.ok(inquiryService.getMyInquiries());
	}
	
	// 문의 수정
	@PatchMapping("/inquiry/{inquiryId}")
	public ResponseEntity<Void> updateInquiry(@PathVariable("inquiryId") Long id, @RequestBody InquiryRequest dto) {
		inquiryService.updateInquiry(id, dto);
		return ResponseEntity.ok().build();
	}
	
	// 문의 삭제
	@DeleteMapping("/inquiry/{inquiryId}")
	public ResponseEntity<Void> deleteInquiry(@PathVariable("inquiryId") Long id){
		inquiryService.deleteInquiry(id);
		return ResponseEntity.noContent().build();
	}
	

	/// 관리자

	// 문의 내역
	@GetMapping("/admin/inquiries")
	public ResponseEntity<List<InquiryResponse>> getAllInquiriesForAdmin(){
		List<InquiryResponse>responses = inquiryService.getAllInquiries();
		return ResponseEntity.ok(responses);
	}
	
	// 문의 답변 -> 유저 이메일 전송
	@PatchMapping("/admin/inquiry/{inquiryId}")
	public ResponseEntity<Void> answer(@PathVariable("inquiryId") Long id, @RequestBody InquiryRequest dto){
		inquiryService.answerInquiry(id, dto.getReplyContent());
		return ResponseEntity.ok().build();
	}
}
package com.ezwell.backend.domain.inquiry;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezwell.backend.domain.user.User;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>{
	
	// 유저 문의 목록
	@EntityGraph(attributePaths = {"user"})
	List<Inquiry> findAllByUserOrderByCreatedAtDesc(User user);
	// JAP 유저별+생성일 조회
	
	// 관리자용 목록
	List<Inquiry> findAllByInquiryCreatedAtDesc();
}

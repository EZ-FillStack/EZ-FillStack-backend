package com.ezwell.backend.domain.inquiry;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezwell.backend.domain.user.User;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>{
	
	// 유저 문의 목록
	@EntityGraph(attributePaths = {"user"})
	List<Inquiry> findAllByUserInquiryByCreateAtDesc(User user);
	
	// 관리자용 목록
	List<Inquiry> findAllByInquiryCreatedAtDesc();
}

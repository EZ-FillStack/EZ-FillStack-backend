package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezwell.backend.domain.event.Event;

public interface SearchRepository extends JpaRepository<Event,Long>{
	@Query("""
			SELECT e FROM Event e
			WHERE e.title LIKE %:keyword% 
			OR e.description LIKE %:keyword% 
			""")
	
	Page<Event> findByTitleContainingOrDescriptionContaining(
			@Param("keyword") String keyword, Pageable  pageable);
}

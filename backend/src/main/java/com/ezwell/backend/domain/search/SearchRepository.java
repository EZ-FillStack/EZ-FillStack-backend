package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezwell.backend.domain.event.Event;

public interface SearchRepository extends JpaRepository<Event,Long>{
	@Query("""
			SELECT e FROM event e
			WHERE e.title LIKE %:keyword% ESCAPE '\\\\'
			OR e.description LIKE %:keyword% ESCAPE '\\\\'
			""")
	
	Page<Event> findByTitleContainingOrDescriptionContaining(
			@Param("keyword") String keyword, Pageable  pageable);
}

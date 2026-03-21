package com.ezwell.backend.domain.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezwell.backend.domain.event.Event;

public interface SearchRepository extends JpaRepository<Event,Long>{
	Page<Event> findByTitleContainingOrDescriptionContaining(String keyword, Pageable  pageable);
}

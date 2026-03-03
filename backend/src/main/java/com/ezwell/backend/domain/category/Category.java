package com.ezwell.backend.domain.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 엔티티
 * - 이벤트를 분류하기 위한 도메인
 * - 관리자에서 생성/수정/삭제 관리
 * - displayOrder 기준으로 정렬
 */
@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 카테고리 이름
     * 예: 공연, 전시, 스포츠, 세미나 등
     */
    private String name;

    /**
     * 상태값 (현재는 String으로 단순 관리)
     * 예: ACTIVE, INACTIVE 등
     * ※ 토이 프로젝트 기준으로 단순 문자열 사용
     */
    private String status;

    /**
     * 화면에 표시될 정렬 순서
     * 숫자가 낮을수록 상단에 노출
     */
    private Integer displayOrder;
}
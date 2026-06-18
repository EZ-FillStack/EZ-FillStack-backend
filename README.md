# E.GO

대규모 트래픽 환경에서도 안정적인 정합성을 보장하는 이벤트 참여 및 대기열 관리 자동화 플랫폼

EZ-FillStack은 다양한 기술 스택 공유 및 네트워킹 이벤트를 개설하고, 유저들이 편리하게 신청하며, 관리자가 이를 효율적으로 제어하는 서비스입니다. 선착순 예매 및 트래픽 폭주 상황에서의 동시성 제어와 아키텍처 안정성에 초점을 맞추어 개발되었습니다.

<br>

## 프로젝트 핵심 지표
* 순간 동시 접속 5,000명 대규모 스파이크 트래픽 안정적 수용 (서버 다운타임 0%)
* 최전방 Kafka 버퍼 구축을 통해 피크 타임 대기열 지연율 0% 달성
* Redis 분산 락 기반 동시성 제어로 데이터 초과 예약률 0% 보장

<br>

## Tech Stacks
* Backend: Java 17, Spring Boot 3.5, Spring Security, OAuth2, JWT
* Data & Cache: MySQL, Redis (Redisson)
* Message Queue: Apache Kafka
* Build Tool: Gradle

<br>

## Git 협업 규칙 및 브랜치 전략
* main: 배포 가능한 상태의 안정적인 소스 코드가 관리되는 메인 브랜치
* develop: 기능 개발 브랜치들의 통합 및 테스트를 진행하는 개발 브랜치
* feature/{담당도메인}: 각 팀원별 도메인 기능 구현을 위한 개별 작업 브랜치 (예: feature/event, feature/auth 등)

<br>

## 팀원별 역할 분담 및 상세 구현 내용

### 이지수 (BE, PM) - event, category, admin, scheduler
* 관리자 통합 조회 성능 최적화: 대용량 신청 목록 및 특정 이벤트 조회 시 연관 엔티티(User, Event)로 인해 발생하는 N+1 쿼리 문제를 해결하기 위해 Repository 계층에 @EntityGraph 적용 및 단일 쿼리 Fetch Join 수행
* 데이터 정합성 보장 로직: 관리자가 신청 상태를 변경하거나 강제 취소할 때, 이벤트 테이블의 현재 참가자 수(currentParticipants)를 실시간으로 연동·증감시키는 트리거 로직 구현을 통한 초과 예약 방지
* 관리 시스템 구조 설계: 이벤트 개설, 카테고리 분류 관리 및 시스템 자동화를 위한 스케줄러 인프라 구축

### 이혜인 (BE) - application, bookmark, inquiry, search
* 분산 환경 동시성 제어: 다수 유저가 동일 좌석에 동시에 접근할 때 발생하는 Race Condition을 방지하고자 Redis 분산 락(Distributed Lock) 도입. tryLock 타임아웃 프로세스를 구축하여 비관적 락의 DB 커넥션 무한 대기 및 데드락 현상 원천 차단
* 대기열 버퍼 레이어 구현: 예매 시작 직후 대량의 스파이크성 트래픽이 메인 DB로 몰려 Connection Pool이 고갈되는 것을 방지하기 위해 최전방에 Apache Kafka 도입. 대기열 진입 요청을 메시지 큐에 선적재한 후 비동기 순차 쓰로틀링(Throttling) 처리로 아키텍처 안정성 확보
* 신청서 도메인 및 검색 엔진 구현: 이벤트 신청 프로세스(application) 구조 설계, 유저 편의를 위한 북마크 및 1:1 문의(inquiry) 시스템, 대용량 이벤트를 빠르게 탐색하기 위한 검색(search) 로직 최적화

### 이인성 (BE) - auth, user, security, notification
* OAuth2 + JWT 기반 인증 시스템: 구글 소셜 로그인 연동 및 안전한 토큰 기반 인증 체계 구축, API 키 등 민감 정보를 다루기 위한 보안 환경 설정
* 엔티티 영속성 관리 및 데이터 보호: 유저 이탈이나 데이터 삭제 요청 시 데이터 유실을 막기 위해 @SQLDelete 및 @Where를 활용한 논리 삭제(Soft Delete) 전략 전면 적용 및 회원 데이터 라이프사이클 이력 보존
* 보안 및 알림 시스템 인프라: Spring Security 기반의 역할 기반 권한 제어(RBAC) 적용으로 유저와 관리자 접근 권한 명확히 분리, 이벤트 상태 변경 및 대기열 진입 시 유저에게 실시간 정보를 전달하는 알림(notification) 서비스 구현

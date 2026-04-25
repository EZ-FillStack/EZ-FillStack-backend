package com.ezwell.backend.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalEventService {

    private final KopisApiClient kopisApiClient;

    // 1. 공연 목록 조회
    public String getPerformances(String stdate, String eddate, int cpage, int rows) {
        return kopisApiClient.getPerformances(stdate, eddate, cpage, rows);
    }

    // 2. 특정 공연 상세 데이터 조회
    public String getEventDetail(String eventId) {
        return kopisApiClient.getPerformanceDetail(eventId);
    }
}
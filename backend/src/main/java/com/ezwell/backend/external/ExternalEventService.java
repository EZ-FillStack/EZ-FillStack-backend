package com.ezwell.backend.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalEventService {

    private final KopisApiClient kopisApiClient;

    // 외부 공연 데이터 조회
    public String getPerformances() {
        return kopisApiClient.getPerformances();
    }
}
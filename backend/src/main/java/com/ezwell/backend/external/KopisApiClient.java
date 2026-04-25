package com.ezwell.backend.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KopisApiClient {

    @Value("${kopis.api.key}")
    private String serviceKey;

    @Value("${kopis.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. KOPIS 공연 목록 조회
    public String getPerformances(String stdate, String eddate, int cpage, int rows) {

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("service", serviceKey)
                .queryParam("stdate", stdate)
                .queryParam("eddate", eddate)
                .queryParam("cpage", cpage)
                .queryParam("rows", rows)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    // 2. KOPIS 특정 공연 상세 조회
    public String getPerformanceDetail(String eventId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/" + eventId)
                .queryParam("service", serviceKey)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
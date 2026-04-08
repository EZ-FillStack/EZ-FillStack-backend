package com.ezwell.backend.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KopisApiClient {

    // 공연 목록 조회 API 호출 메서드
    public String getPerformances() {


        // service = 발급받은 인증키
        // stdate / eddate = 조회 기간
        // cpage = 페이지
        // rows = 가져올 개수
        String url =
                "http://www.kopis.or.kr/openApi/restful/pblprfr"
                        + "?service=86b2cab5099c41b6b0a208dab4131f62"
                        + "&stdate=20260101"
                        + "&eddate=20261231"
                        + "&cpage=1"
                        + "&rows=10";

        // Spring에서 제공하는 HTTP 요청 객체
        RestTemplate restTemplate = new RestTemplate();


        // GET 요청 보내고 결과(XML)를 문자열로 반환
        return restTemplate.getForObject(url, String.class);
    }
}
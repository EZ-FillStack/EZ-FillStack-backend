package com.ezwell.backend.external;

import com.ezwell.backend.external.dto.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalEventService {

    private final KopisApiClient kopisApiClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    // 1. 목록 조회
    public List<KopisEventDto> getPerformances(String stdate, String eddate, int cpage, int rows) {
        String xmlData = kopisApiClient.getPerformances(stdate, eddate, cpage, rows);

        try {
            KopisResponse response = xmlMapper.readValue(xmlData, KopisResponse.class);
            if (response != null && response.getDb() != null) {
                return response.getDb();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    // 2. 상세 조회
    public KopisEventDetailDto getEventDetail(String eventId) {
        // 공연 상세 정보 호출
        String xmlData = kopisApiClient.getPerformanceDetail(eventId);

        try {
            KopisDetailResponse response = xmlMapper.readValue(xmlData, KopisDetailResponse.class);

            if (response != null && response.getDb() != null) {
                KopisEventDetailDto eventDetail = response.getDb();

                if (eventDetail.getFacilityId() != null) {
                    String facilityXml = kopisApiClient.getFacilityDetail(eventDetail.getFacilityId());
                    KopisFacilityResponse facilityResponse = xmlMapper.readValue(facilityXml, KopisFacilityResponse.class);

                    if (facilityResponse != null && facilityResponse.getDb() != null) {
                        eventDetail.setAddress(facilityResponse.getDb().getAddress());
                        eventDetail.setLatitude(facilityResponse.getDb().getLatitude());
                        eventDetail.setLongitude(facilityResponse.getDb().getLongitude());
                    }
                }

                return eventDetail;
            }
        } catch (Exception e) {
            System.err.println("KOPIS 상세/시설 XML 파싱 에러: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
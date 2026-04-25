package com.ezwell.backend.external;

import com.ezwell.backend.external.dto.KopisEventDetailDto;
import com.ezwell.backend.external.dto.KopisEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/events")
public class ExternalController {

    private final ExternalEventService externalEventService;

    // 1. KOPIS 실시간 공연 목록 조회
    @GetMapping
    public String getExternalEvents(
            @RequestParam(name = "stdate", defaultValue = "20260101") String stdate,
            @RequestParam(name = "eddate", defaultValue = "20261231") String eddate,
            @RequestParam(name = "cpage", defaultValue = "1") int cpage,
            @RequestParam(name = "rows", defaultValue = "10") int rows
    ) {
        return externalEventService.getPerformances(stdate, eddate, cpage, rows).toString();
    }

    // 2. KOPIS 특정 공연 상세 데이터 조회
    @GetMapping("/{kopisEventId}")
    public KopisEventDetailDto getExternalEventDetail(@PathVariable("kopisEventId") String kopisEventId) {
        return externalEventService.getEventDetail(kopisEventId);
    }
}
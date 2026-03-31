package com.ezwell.backend.external;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/events")
public class ExternalController {

    private final ExternalEventService externalEventService;

    @GetMapping
    public String getExternalEvents() {
        return externalEventService.getPerformances();
    }
}
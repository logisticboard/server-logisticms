package com.example.logisticms.controller;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.LogisticsActivityAiContextRequest;
import com.example.logisticms.dto.LogisticsAiContextResponse;
import com.example.logisticms.service.impl.LogisticsAiQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/internal/v1/ai")
@RequiredArgsConstructor
public class InternalAiController {
    @Value("${INTERNAL_API_TOKEN}")
    private String internalApiToken;

    private final LogisticsAiQueryService logisticsAiQueryService;

    @PostMapping("/context")
    public ApiResponseDTO<String> getAiContext(
            @RequestHeader("X-Internal-Api-Key") String internalApiKey,
            @RequestBody LogisticsActivityAiContextRequest request
    ) {
        if (!internalApiToken.equals(internalApiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal api key");
        }

        return ApiResponseDTO.<String>builder()
                .success(true)
                .message("AI context fetched successfully")
                .data(logisticsAiQueryService.fetchContext(request))
                .build();
    }
}

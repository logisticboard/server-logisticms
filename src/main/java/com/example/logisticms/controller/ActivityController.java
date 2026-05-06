package com.example.logisticms.controller;

import com.example.logisticms.dto.ActivityListResponse;
import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.service.impl.ActivityService;
import com.example.logisticms.service.util.UpstreamHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/activity/v1")
@RequiredArgsConstructor
@Validated
public class ActivityController {
    private final ActivityService activityService;
    @GetMapping("/{fleetOperatorId}")
    public ApiResponseDTO<List<ActivityListResponse>> getActivity(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId){
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        List<ActivityListResponse> activityListResponse = activityService.getActivityList(userId, fleetOperatorId);
        return ApiResponseDTO.success(httpServletRequest.getRequestURI(), "Activity list fetched", activityListResponse);
    }
}

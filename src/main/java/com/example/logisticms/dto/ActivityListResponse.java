package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ActivityListResponse {
    private UUID conversationId;
    private String shipmentFormalName;
    private UUID shipmentId;
    private String pickupLocation;
    private String dropLocation;
    private LocalDateTime lastActivityAt;
    private String shipmentName;
}

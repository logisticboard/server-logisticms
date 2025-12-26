package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverFleetOperatorResponse {
    private String fleetOperatorName;
    private UUID fleetOperatorUid;
}

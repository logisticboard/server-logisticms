package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfileOverviewResponse {
    private String shipmentCompleted;
    private String shipmentInTransit;
    private String shipmentPending;
    private String totalShipments;
}

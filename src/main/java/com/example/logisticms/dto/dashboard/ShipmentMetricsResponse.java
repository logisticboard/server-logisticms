package com.example.logisticms.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShipmentMetricsResponse {
    private RangeDTO range;
    private List<ShipmentMetricsSeriesDTO> series;
}


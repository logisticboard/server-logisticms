package com.example.logisticms.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ShipmentMetricsSeriesDTO {
    private String date;
    private Map<String, Integer> statusCounts;

    public ShipmentMetricsSeriesDTO(String date, Map<String, Integer> statusCounts) {
        this.date = date;
        this.statusCounts = statusCounts;
    }
}

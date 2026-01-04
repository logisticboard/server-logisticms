package com.example.logisticms.service;

import com.example.logisticms.dto.dashboard.ShipmentMetricsResponse;
import java.time.LocalDate;
import java.util.UUID;

public interface ShipmentMetricsService {
    ShipmentMetricsResponse getShipmentMetrics(UUID fleetOperatorId, LocalDate startDate, LocalDate endDate);
}


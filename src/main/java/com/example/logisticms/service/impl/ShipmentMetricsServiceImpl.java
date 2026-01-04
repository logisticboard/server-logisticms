package com.example.logisticms.service.impl;

import com.example.logisticms.dto.dashboard.RangeDTO;
import com.example.logisticms.dto.dashboard.ShipmentMetricsResponse;
import com.example.logisticms.dto.dashboard.ShipmentMetricsSeriesDTO;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.service.ShipmentMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShipmentMetricsServiceImpl implements ShipmentMetricsService {
    private final ShipmentRepository shipmentRepository;

    @Override
    public ShipmentMetricsResponse getShipmentMetrics(UUID fleetOperatorId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Object[]> results = shipmentRepository.countShipmentsByDateAndStatus(fleetOperatorId, startDateTime, endDateTime);
        Map<String, Map<String, Integer>> dateStatusMap = new LinkedHashMap<>();
        for (Object[] row : results) {
            String date = row[0].toString();
            String status = row[1].toString();
            Integer count = ((Number) row[2]).intValue();
            dateStatusMap.computeIfAbsent(date, k -> new HashMap<>());
            dateStatusMap.get(date).put(status, count);
        }
        List<ShipmentMetricsSeriesDTO> series = new ArrayList<>();
        Map<String, Integer> prefixSum = new HashMap<>();
        for (ShipmentStatus status : ShipmentStatus.values()) {
            prefixSum.put(status.name(), 0);
        }
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            String dateStr = current.toString();
            Map<String, Integer> statusCounts = new HashMap<>();
            for (ShipmentStatus status : ShipmentStatus.values()) {
                int todayCount = dateStatusMap.getOrDefault(dateStr, Collections.emptyMap()).getOrDefault(status.name(), 0);
                int sum = prefixSum.get(status.name()) + todayCount;
                statusCounts.put(status.name(), sum);
                prefixSum.put(status.name(), sum);
            }
            series.add(new ShipmentMetricsSeriesDTO(dateStr, statusCounts));
            current = current.plusDays(1);
        }
        RangeDTO range = new RangeDTO(startDate.toString(), endDate.toString(), "DAY");
        ShipmentMetricsResponse response = new ShipmentMetricsResponse();
        response.setRange(range);
        response.setSeries(series);
        return response;
    }
}

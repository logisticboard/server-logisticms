package com.example.logisticms.service.impl;


import com.example.logisticms.dto.TrackingDto;
//import com.example.logisticms.entity.Shipment;
//import com.example.logisticms.repository.DriverRepository;
//import com.example.logisticms.repository.LocationUpdateRepository;
//import com.example.logisticms.repository.ShipmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import com.example.logisticms.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl {
    private final TrackingRepository trackingRepository;
    public List<TrackingDto> getShipmentTracking(UUID shipmentId) {
        return trackingRepository.findByShipment_IdOrderByTimestampAsc(shipmentId).stream()
                .map(tracking -> TrackingDto.builder()
                        .heading(tracking.getHeading())
                        .description(tracking.getDescription())
                        .timestamp(tracking.getTimestamp())
                        .shipmentStatus(tracking.getShipmentStatus())
                        .build())
                .toList();
    }

}


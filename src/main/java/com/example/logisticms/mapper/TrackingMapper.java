package com.example.logisticms.mapper;

import com.example.logisticms.dto.TrackingDto;
import com.example.logisticms.entity.Tracking;

public class TrackingMapper {
    public static TrackingDto trackingDto(Tracking tracking){
        return TrackingDto.builder()
                .heading(tracking.getHeading())
                .description(tracking.getDescription())
                .timestamp(tracking.getTimestamp())
                .shipmentStatus(tracking.getShipmentStatus())
                .build();
    }
}

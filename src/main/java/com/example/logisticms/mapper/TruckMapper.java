package com.example.logisticms.mapper;

import com.example.logisticms.dto.TruckDto;
import com.example.logisticms.entity.Truck;

public class TruckMapper {
    public static TruckDto toDto(Truck truck) {
        return TruckDto.builder()
                .truckId(truck.getId())
                .registrationNumber(truck.getRegistrationNumber())
                .model(truck.getModel())
                .capacity(truck.getCapacity())
                .description(truck.getDescription())
                .truckStatus(truck.getStatus())
                .build();
    }
}

package com.example.logisticms.mapper;

import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.dto.DriverShipment;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.DriverStatus;

public class DriverMapper {
    public static Driver toEntity(com.example.logisticms.dto.DriverDto dto) {
        if (dto == null) {
            return null;
        }
        return Driver.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .licenseNumber(dto.getLicenseNumber())
                .currentLat(dto.getCurrentLat())
                .currentLon(dto.getCurrentLon())
                .build();
    }


    public static DriverDto toDto(Driver entity) {
        if (entity == null) {
            return null;
        }
        return DriverDto.builder()
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .licenseNumber(entity.getLicenseNumber())
                .currentLat(entity.getCurrentLat())
                .currentLon(entity.getCurrentLon())
                .build();
    }
}


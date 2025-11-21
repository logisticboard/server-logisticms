package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DriverDto {
    private String name;
    private String phoneNumber;
    private String licenseNumber;

    private Double currentLat;
    private Double currentLon;
}

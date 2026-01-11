package com.example.logisticms.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DriverLocationUpdateRequest {
    private Double latitude;
    private Double longitude;
    private String address;
}


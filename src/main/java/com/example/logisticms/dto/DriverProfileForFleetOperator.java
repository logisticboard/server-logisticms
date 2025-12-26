package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverProfileForFleetOperator {
    private String fullName;
    private String licenseNumber;
}

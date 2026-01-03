package com.example.logisticms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoordinateDTO {
    private Double latitude;
    private Double longitude;
}


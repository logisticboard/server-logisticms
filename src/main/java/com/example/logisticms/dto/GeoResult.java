package com.example.logisticms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoResult {
    private double lat;
    private double lng;
    private String address;
}

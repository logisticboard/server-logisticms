package com.example.logisticms.dto;

import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.Shipment;

import java.util.List;

public class TruckDto {
    private String registrationNumber;
    private String model;
    private Double capacity;
    private String description;
    private List<String> assignedDriver; //email of assigned drivers
    private List<Shipment> shipments;
}

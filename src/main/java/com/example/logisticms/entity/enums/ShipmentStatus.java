package com.example.logisticms.entity.enums;

public enum ShipmentStatus {
    CREATED,
    TRUCK_ASSIGNED,
    READY_FOR_DISPATCH, //driver + truck assigned
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}


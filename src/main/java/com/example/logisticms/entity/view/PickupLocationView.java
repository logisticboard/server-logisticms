package com.example.logisticms.entity.view;

import com.example.logisticms.entity.Location;

import java.util.UUID;

public interface PickupLocationView {
    Location getPickupLocation();
    UUID getShipmentId();
}

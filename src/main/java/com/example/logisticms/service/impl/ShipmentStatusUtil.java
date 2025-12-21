package com.example.logisticms.service.impl;


import com.example.logisticms.dto.ShipmentStatusInfo;
import com.example.logisticms.entity.enums.ShipmentStatus;

import java.util.EnumMap;
import java.util.Map;

public final class ShipmentStatusUtil {

    private static final Map<ShipmentStatus, ShipmentStatusInfo> STATUS_INFO_MAP;

    static {
        STATUS_INFO_MAP = new EnumMap<>(ShipmentStatus.class);

        STATUS_INFO_MAP.put(
                ShipmentStatus.CREATED,
                new ShipmentStatusInfo(
                        "Shipment Created",
                        "The shipment has been created and is awaiting truck assignment."
                )
        );

        STATUS_INFO_MAP.put(
                ShipmentStatus.TRUCK_ASSIGNED,
                new ShipmentStatusInfo(
                        "Truck Assigned",
                        "A truck has been assigned to this shipment."
                )
        );

        STATUS_INFO_MAP.put(
                ShipmentStatus.READY_FOR_DISPATCH,
                new ShipmentStatusInfo(
                        "Ready for Dispatch",
                        "The shipment is ready for dispatch with driver and truck assigned."
                )
        );

        STATUS_INFO_MAP.put(
                ShipmentStatus.IN_TRANSIT,
                new ShipmentStatusInfo(
                        "Shipment In Transit",
                        "The shipment is currently in transit to the delivery location."
                )
        );

        STATUS_INFO_MAP.put(
                ShipmentStatus.DELIVERED,
                new ShipmentStatusInfo(
                        "Shipment Delivered",
                        "The shipment has been successfully delivered."
                )
        );

        STATUS_INFO_MAP.put(
                ShipmentStatus.CANCELLED,
                new ShipmentStatusInfo(
                        "Shipment Cancelled",
                        "The shipment has been cancelled and will not be processed further."
                )
        );
    }

    private ShipmentStatusUtil() {}

    public static ShipmentStatusInfo getInfo(ShipmentStatus status) {
        return STATUS_INFO_MAP.getOrDefault(
                status,
                new ShipmentStatusInfo("Unknown Status", "Shipment status is unavailable.")
        );
    }

    public static String getHeading(ShipmentStatus status) {
        return getInfo(status).heading();
    }

    public static String getDescription(ShipmentStatus status) {
        return getInfo(status).description();
    }
}

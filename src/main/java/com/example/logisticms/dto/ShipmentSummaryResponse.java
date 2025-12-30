package com.example.logisticms.dto;

import com.example.logisticms.entity.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentSummaryResponse {
    List<Shipment> shipments;
    private int totalShipments;
    private int inTransit;
    private int delivered;
    private int pending;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Shipment{
        private UUID shipmentId;
        private String shipmentName;
        private String shipmentFormalName;
        private String pickupLocationAddress;
        private String deliveryLocationAddress;
        private ShipmentStatus shipmentStatus;
        private double shipmentWeight;
        private LocalDateTime shipmentPickupDate;
    }
}

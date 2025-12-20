package com.example.logisticms.dto;

import com.example.logisticms.entity.ContactDetails;
import com.example.logisticms.entity.enums.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverShipment {
    private String shipmentName;
    private ShipmentStatus shipmentStatus;
    private String shipmentId;
    private String pickupLocationAddress;
    private String deliveryLocationAddress;
    private LocalDateTime shipmentPickupDate;
    private Double shipmentWeight;
    private String shipmentSpecialInstructions;
    private String fleetOperatorName;
    List<ShipmentProgress> shipmentProgressList;
    List<ShipmentCreateRequest.ContactDetailsRequest> contactDetails;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentProgress {
        private String heading;
        private String description;
        private LocalDateTime timestamp;
        private ShipmentStatus shipmentStatus;
    }
}

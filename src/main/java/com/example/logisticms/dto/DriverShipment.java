package com.example.logisticms.dto;

import com.example.logisticms.entity.ContactDetails;
import com.example.logisticms.entity.enums.ShipmentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverShipment {
    private UUID shipmentUid;
    private String shipmentName;
    private ShipmentStatus shipmentStatus;
    private String shipmentId;
    private String pickupLocationAddress;
    private String deliveryLocationAddress;
    private LocalDateTime shipmentPickupDate;
    private Double shipmentWeight;
    private String shipmentSpecialInstructions;
    private String fleetOperatorName;
    List<ShipmentCreateRequest.ContactDetailsRequest> contactDetails;
}

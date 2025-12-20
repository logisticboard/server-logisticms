package com.example.logisticms.dto;

import com.example.logisticms.entity.enums.ShipmentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class ShipmentCreateResponse {
    private UUID shipmentId;
    private String shipmentFormalName;
    private String shipmentName;
    private LocalDateTime pickupDate;
    private String pickupLocation;
    private String deliveryLocation;
    private Double shipmentWeight; // changed from String → Double
    private Double shipmentTotalEstimatedCost; // changed from String → Double
    private String truckId;
    private String shipmentSpecialInstructions;
    private ShipmentStatus shipmentStatus;
    private List<ShipmentCreateRequest.ContactDetailsRequest> contactDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDetailsRequest {
        private String name;
        private String role;
        private String email;
        private String phoneNumber;
    }
}

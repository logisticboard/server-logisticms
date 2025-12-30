package com.example.logisticms.dto;


import com.example.logisticms.entity.enums.ShipmentAssignment;
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
public class ShipmentDetailsResponse {
    private UUID id;
    private String shipmentName;
    private String shipmentFormalName;
    private LocalDateTime pickupDate;
    private String pickupLocation;
    private String deliveryLocation;
    private Double shipmentWeight;
    private Double shipmentTotalEstimatedCost;
    private String shipmentSpecialInstructions;
    private ShipmentStatus shipmentStatus;
    private List<ShipmentAssignmentData> assignments;
    private String fleetOperatorName;
    private List<ContactDetailsData> contactDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentAssignmentData{
        private String truckUid;
        private String truckRegistrationNumber;
        private String truckModel;
        private Double truckCapacity;
        private List<DriverData> drivers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriverData{
        private String driverUid;
        private String driverFullName;
        private String driverLicenseNumber;
        private String driverContactNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDetailsData{
        private String contactName;
        private String contactEmail;
        private String contactPhone;
        private String contactRole;
    }
}

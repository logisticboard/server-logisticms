package com.example.logisticms.dto;

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
public class ShipmentCreateRequest {

    @NotBlank(message = "Shipment name is required")
    private String shipmentName;

    @NotNull(message = "Pickup date is required")
    @Future(message = "Pickup date must be in the future")
    private LocalDateTime pickupDate;

    @Valid
    private Location pickupLocation;

    @Valid
    private Location deliveryLocation;

    @NotNull(message = "Shipment weight is required")
    @Positive(message = "Shipment weight must be positive")
    private Double shipmentWeight; // changed from String → Double

    @NotNull(message = "Estimated cost is required")
    @PositiveOrZero(message = "Estimated cost cannot be negative")
    private Double shipmentTotalEstimatedCost; // changed from String → Double

    private List<@Valid ShipperDataDto> shippers;

    @Size(max = 500, message = "Special instructions cannot exceed 500 characters")
    private String shipmentSpecialInstructions;

    @NotEmpty(message = "At least one contact detail is required")
    private List<@Valid ContactDetailsRequest> contactDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        private Double longitude;

        @NotBlank(message = "Address is required")
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDetailsRequest {

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Role is required")
        private String role;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10,15}$",
                message = "Phone number must contain 10–15 digits"
        )
        private String phoneNumber;
    }

    @Data
    public static class ShipperDataDto {
        @NotNull
        private UUID truckUid;
        @NotNull
        private List<UUID> driverUids;
    }
}

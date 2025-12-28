package com.example.logisticms.dto;
import com.example.logisticms.entity.DriverStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class DriverDto {

    private UUID driverId;

    @NotBlank(message = "Driver name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;


    @NotNull(message = "Driver status is required")
    private DriverStatus driverStatus;

    // Optional but must be valid if present
    @DecimalMin(value = "-90.0", message = "Latitude cannot be less than -90.0")
    @DecimalMax(value = "90.0", message = "Latitude cannot be greater than 90.0")
    private BigDecimal currentLat;

    @DecimalMin(value = "-180.0", message = "Longitude cannot be less than -180.0")
    @DecimalMax(value = "180.0", message = "Longitude cannot be greater than 180.0")
    private BigDecimal currentLon;
}

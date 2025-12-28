package com.example.logisticms.dto;

import com.example.logisticms.entity.TruckStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckDto {

    private UUID truckId;

    @NotBlank(message = "Registration number cannot be blank")
    @Size(min = 5, max = 20, message = "Registration number must be between 5 and 20 characters")
    private String registrationNumber;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be a positive value")
    private Double capacity;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Truck status is required")
    private TruckStatus truckStatus;


}

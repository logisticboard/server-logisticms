package com.example.logisticms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FleetOperatorDto {

    private UUID fleetOperatorId;

    // Optional field
    private String imageUrl;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name can't exceed 100 characters")
    @JsonProperty("companyName")
    private String name;

    @NotBlank(message = "GST Number is required")
    @Pattern(
            regexp = "^[0-9A-Z]{15}$",
            message = "GST Number must be a valid 15-character alphanumeric GSTIN"
    )
    @JsonProperty("gstNumber")
    private String gstNumber;

    @NotBlank(message = "Contact phone is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Contact phone must be a valid 10-digit number"
    )
    @JsonProperty("phoneNumber")
    private String contactPhone;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String contactEmail;

    @NotBlank(message = "Address is required")
    @Size(max = 250, message = "Address can't exceed 250 characters")
    @JsonProperty("address")
    private String address;


}


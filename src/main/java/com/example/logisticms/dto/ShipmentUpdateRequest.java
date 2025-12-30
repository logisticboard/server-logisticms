package com.example.logisticms.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentUpdateRequest {
    private List<ShipmentCreateRequest.ShipperDataDto> shippers;

    @NotEmpty(message = "At least one contact detail is required")
    @Valid
    private List<ShipmentCreateRequest.ContactDetailsRequest> contactDetails;
}

package com.example.logisticms.controller;



import com.example.logisticms.dto.*;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import com.example.logisticms.service.impl.ShipmentServiceImpl;
import com.example.logisticms.service.impl.TruckServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class ShipmentController {

    private final ShipmentServiceImpl shipmentService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;
    private final TruckServiceImpl truckService;
    private final FleetOperatorServiceImpl fleetOperatorService;


    @PostMapping("/fleetoperators/{fleetOperatorId}/shipments")
    public ApiResponseDTO<ShipmentCreateResponse> createShipment(@RequestBody @Valid ShipmentCreateRequest shipment, @PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)) {
            FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);
            Shipment shipmentResponse = shipmentService.createShipment(shipment, fleetOperator);
            return ApiResponseDTO.<ShipmentCreateResponse>builder()
                    .data(ShipmentMapper.toShipmentCreateResponse(shipmentResponse))
                    .success(Boolean.TRUE)
                    .message("Shipment created successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only admins can create shipments for a fleet operator");
    }

    @PutMapping("/shipments/{shipmentId}")
    public ApiResponseDTO<ShipmentCreateResponse> assignTrucksAndDriversToShipment(@RequestBody @Valid ShipmentUpdateRequest request, @PathVariable UUID shipmentId) {
            shipmentService.updateShipment(request, shipmentId);
            return ApiResponseDTO.<ShipmentCreateResponse>builder()
                    .success(Boolean.TRUE)
                    .message("Trucks and drivers assigned to shipment successfully")
                    .build();
    }



    @GetMapping("/fleetoperators/{fleetOperatorId}/shipments")
    public ApiResponseDTO<ShipmentSummaryResponse> getAllShipmentsSummary(
            @PathVariable UUID fleetOperatorId,
            @RequestParam(value = "shipmentStatus", required = false) ShipmentStatus shipmentStatus,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size) {
        UUID userId =  UUID.fromString((String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            return ApiResponseDTO.<ShipmentSummaryResponse>builder()
                    .data(shipmentService.getAllShipmentsSummary(fleetOperatorId, shipmentStatus, page, size))
                    .success(Boolean.TRUE)
                    .message("Shipment summary fetched successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only members can view shipments for a fleet operator");
    }

    @GetMapping("/shipments/{shipmentId}")
    public ApiResponseDTO<ShipmentDetailsResponse> getShipmentDetails(@PathVariable UUID shipmentId) {
            return ApiResponseDTO.<ShipmentDetailsResponse>builder()
                    .data(shipmentService.getShipmentDetails(shipmentId))
                    .success(Boolean.TRUE)
                    .message("Shipment summary fetched successfully")
                    .build();

    }

    @PutMapping("/shipments/{shipmentId}/status")
    public ApiResponseDTO<Void> updateShipmentStatus(@PathVariable UUID shipmentId, @RequestParam ShipmentStatus status) {
        shipmentService.updateShipmentStatus(status, shipmentId);
        return ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Shipment status updated successfully")
                .build();
    }

}

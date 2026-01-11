package com.example.logisticms.controller;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.service.impl.DriverServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverService;
    private final FleetOperatorServiceImpl fleetOperatorService;


    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/shipments")
    public ApiResponseDTO<List<DriverShipment>> getAllShipmentsForFleetOperatorDriver(  @RequestParam(required = false) ShipmentStatus shipmentStatus,
                                                                                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        String phoneNumber = (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString();
        return ApiResponseDTO.<List<DriverShipment>>builder()
                .message("Shipments retrieved successfully")
                .success(true)
                .data(driverService.getAllShipmentsForDriver(phoneNumber, shipmentStatus, page, size))
                .build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/fleetOperators")
    public ApiResponseDTO<List<DriverFleetOperatorResponse>> getAllFleetOperatorsForDriver() {
        String phoneNumber = (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString();
        return ApiResponseDTO.<List<DriverFleetOperatorResponse>>builder()
                .message("Fleet Operators retrieved successfully")
                .success(true)
                .data(driverService.getAllFleetOperatorsNameForDriver(phoneNumber))
                .build();
    }
    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/fleetOperators/{fleetOperatorId}/profile")
    public ApiResponseDTO<DriverProfileForFleetOperator> getDriverProfileForFleetOperator(@PathVariable UUID fleetOperatorId) {
        String phoneNumber = (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString();
        return ApiResponseDTO.<DriverProfileForFleetOperator>builder()
                .message("Fleet Operators retrieved successfully")
                .success(true)
                .data(driverService.getDriverProfileForFleetOperator(phoneNumber, fleetOperatorId))
                .build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/shipments/{shipmentId}/tracking")
    public ApiResponseDTO<List<TrackingDto>> getAllShipmentsTrackDetails(@PathVariable UUID shipmentId) {
        String phoneNumber = (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString();
        return ApiResponseDTO.<List<TrackingDto>>builder()
                .message("Shipments retrieved successfully")
                .success(true)
                .data(driverService.getShipmentTrack(shipmentId, phoneNumber))
                .build();
    }




    @GetMapping("/{id}")
    public ApiResponseDTO<DriverDto> getDriverById(@PathVariable UUID id) {
//        TODO: instead of uuid id we should use email as user id is confidential
//        TODO: Add authorization to ensure drivers can only access their own profiles or fleetowner of driver can access their drivers
        return ApiResponseDTO.<DriverDto>builder()
                .message("Driver Profile retrieved successfully")
                .success(true)
                .data(driverService.getDriverById(id))
                .build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/shipment/{shipmentId}/location")
    public ApiResponseDTO<Void> updateDriverLocation(@RequestBody DriverLocationUpdateRequest request, @PathVariable UUID shipmentId) {
        String phoneNumber = (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString();
        driverService.updateDriverLocation(shipmentId, phoneNumber, request);
        return ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Driver location updated successfully")
                .build();
    }
}

package com.example.logisticms.controller;


import com.example.logisticms.dto.*;
import com.example.logisticms.dto.dashboard.ShipmentMetricsResponse;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import com.example.logisticms.service.impl.ShipmentMetricsServiceImpl;
import com.example.logisticms.service.impl.ShipmentServiceImpl;
import com.example.logisticms.service.util.UpstreamHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
public class ShipmentController {

    private final ShipmentServiceImpl shipmentService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;
    private final FleetOperatorServiceImpl fleetOperatorService;

    @Autowired
    private ShipmentMetricsServiceImpl shipmentMetricsService;


    @PostMapping("/fleetoperators/{fleetOperatorId}/shipments")
    public ApiResponseDTO<ShipmentCreateResponse> createShipment(HttpServletRequest httpServletRequest, @RequestBody @Valid ShipmentCreateRequest shipment, @PathVariable UUID fleetOperatorId) {
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if (fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)) {
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
            HttpServletRequest httpServletRequest,
            @PathVariable UUID fleetOperatorId,
            @RequestParam(value = "shipmentStatus", required = false) ShipmentStatus shipmentStatus,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "100") int size) {
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if (fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
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

    @GetMapping("/shipments/driver-locations")
    public ApiResponseDTO<Map<UUID, CoordinateDTO>> getDriverLocations(
            @RequestParam(name = "shipmentIds") List<UUID> shipmentIds) {
        return ApiResponseDTO.<Map<UUID, CoordinateDTO>>builder()
                .data(shipmentService.getDriverCurrentLocationsForShipments(shipmentIds))
                .success(true)
                .message("Driver locations fetched successfully")
                .build();
    }

    @GetMapping("/fleetoperators/{fleetOperatorId}/shipments/overview")
    public ApiResponseDTO<ShipmentOverviewResponse> getShipmentOverview(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId) {
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if (fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            return ApiResponseDTO.<ShipmentOverviewResponse>builder()
                    .data(shipmentService.getShipmentOverview(fleetOperatorId))
                    .success(Boolean.TRUE)
                    .message("Shipment overview and transaction history fetched successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only members can view shipment overview and history for a fleet operator");
    }

    @GetMapping("/fleetoperators/{fleetOperatorId}/shipments/transaction-history")
    public ApiResponseDTO<org.springframework.data.domain.Page<TransactionHistoryItemDto>> getTransactionHistory(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID fleetOperatorId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if (fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            return ApiResponseDTO.<org.springframework.data.domain.Page<TransactionHistoryItemDto>>builder()
                    .data(shipmentService.getTransactionHistory(fleetOperatorId, page, size))
                    .success(Boolean.TRUE)
                    .message("Transaction history fetched successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only members can view transaction history for a fleet operator");
    }

    @GetMapping("/fleetoperators/{fleetOperatorId}/dashboard/shipment-metrics")
    public ApiResponseDTO<ShipmentMetricsResponse> getShipmentMetrics(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID fleetOperatorId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if(fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            ShipmentMetricsResponse metrics = shipmentMetricsService.getShipmentMetrics(fleetOperatorId, startDate, endDate);
            return ApiResponseDTO.<ShipmentMetricsResponse>builder()
                    .data(metrics)
                    .success(Boolean.TRUE)
                    .message("Shipment metrics fetched successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only members can view Shipment metrics for a fleet operator");
    }

}

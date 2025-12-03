package com.example.logisticms.controller;



import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.ShipmentCreateRequest;
import com.example.logisticms.dto.ShipmentCreateResponse;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Shipment;
import com.example.logisticms.entity.Truck;
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
@RequestMapping("/api/v1/fleetoperators")
@RequiredArgsConstructor
@Validated
public class ShipmentController {

    private final ShipmentServiceImpl shipmentService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;
    private final TruckServiceImpl truckService;
    private final FleetOperatorServiceImpl fleetOperatorService;

//    TODO: test without truck and with truck

    @PostMapping("{fleetOperatorId}/shipments")
    public ApiResponseDTO<ShipmentCreateResponse> createShipment(@RequestBody @Valid ShipmentCreateRequest shipment, @PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)) {
            Truck truck = null;
            if(shipment.getTruckId() != null) {
                truck = truckService.getTruckByFleetOperatorIdAndTruckId(fleetOperatorId, shipment.getTruckId());
            }
            FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);
            Shipment shipmentResponse = shipmentService.createShipment(shipment, truck, fleetOperator);
            return ApiResponseDTO.<ShipmentCreateResponse>builder()
                    .data(ShipmentMapper.toShipmentCreateResponse(shipmentResponse))
                    .success(Boolean.TRUE)
                    .message("Shipment created successfully")
                    .build();
        }
        throw new UnauthorizedOperationException("Only admins can create shipments for a fleet operator");
    }

    @GetMapping("/{fleetOperatorId}/shipments")
    public ApiResponseDTO<ShipmentSummaryResponse> getAllShipmentsSummary(@PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)) {
            return ApiResponseDTO.<ShipmentSummaryResponse>builder()
                    .data(shipmentService.getAllShipmentsSummary(fleetOperatorId))
                    .success(Boolean.TRUE)
                    .message("Shipment summary fetched successfully")
                    .build();

        }
        throw new UnauthorizedOperationException("Only admins can create shipments for a fleet operator");
    }
//
//    @GetMapping("/{id}")
//    public Shipment getShipmentById(@PathVariable Long id) {
//        return shipmentService.getShipmentById(id);
//    }
//
//    @PutMapping("/{id}/status")
//    public Shipment updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
//        return shipmentService.updateStatus(id, status);
//    }

//    @PutMapping("/{id}/assign")
//    public Shipment assignTruckAndDriver(@PathVariable Long id,
//                                         @RequestParam Long truckId,
//                                         @RequestParam Long driverId) {
//        return shipmentService.assignTruckAndDriver(id, truckId, driverId);
//    }

//    @DeleteMapping("/{id}")
//    public void deleteShipment(@PathVariable Long id) {
//        shipmentService.deleteShipment(id);
//    }
}


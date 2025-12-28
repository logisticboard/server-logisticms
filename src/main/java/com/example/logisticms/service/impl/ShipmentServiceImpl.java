package com.example.logisticms.service.impl;


import com.example.logisticms.dto.ShipmentCreateRequest;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentAssignment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl {

    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;

    public Shipment createShipment(ShipmentCreateRequest shipment, FleetOperator fleetOperator) {
        ShipmentStatus shipmentStatus = ShipmentStatus.CREATED;
        Shipment toSave = ShipmentMapper.toEntity(shipment, shipmentStatus, fleetOperator);
        List<ShipmentAssignment> shipmentAssignments = new ArrayList<>();
        if(shipment.getShippers() != null && !shipment.getShippers().isEmpty()) {
            boolean isDriverAssignedToAllShippers = true;
            for(ShipmentCreateRequest.ShipperDataDto shipper : shipment.getShippers()) {
                if(shipper.getDriverUids().isEmpty())
                    isDriverAssignedToAllShippers = false;
                for(UUID driverUid : shipper.getDriverUids()) {
                    shipmentAssignments.add(ShipmentAssignment.builder()
                                    .truck(Truck.builder().id(shipper.getTruckUid()).build())
                                    .driver(Driver.builder().id(driverUid).build())
                                    .shipment(toSave)
                            .build());
                }
            }
            toSave.setAssignments(shipmentAssignments);
            shipmentStatus = isDriverAssignedToAllShippers ? ShipmentStatus.READY_FOR_DISPATCH:ShipmentStatus.TRUCK_ASSIGNED;
        }
        Shipment shipmentSavedEntity = shipmentRepository.save(toSave);
        trackingRepository.save(Tracking.builder()
                .shipment(shipmentSavedEntity)
                .heading("Shipment Created")
                .description("Shipment has been created.")
                .timestamp(shipmentSavedEntity.getCreatedAt())
                .shipmentStatus(shipmentStatus)
                .build());
        return shipmentSavedEntity;
    }

    public ShipmentSummaryResponse getAllShipmentsSummary(UUID fleetOperatorId) {
        List<ShipmentSummaryResponse.Shipment> shipments = shipmentRepository.findAllByFleetOperator_Id(fleetOperatorId).stream()
                .map(ShipmentMapper::toShipmentSummaryShipmentResponse)
                .toList();
        return ShipmentSummaryResponse.builder()
                .shipments(shipments)
                .totalShipments(shipments.size())
                .inTransit((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.IN_TRANSIT).count())
                .delivered((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.DELIVERED).count())
                .pending((int) shipments.stream().filter(s -> s.getShipmentStatus() == ShipmentStatus.CREATED || s.getShipmentStatus() == ShipmentStatus.TRUCK_ASSIGNED || s.getShipmentStatus() == ShipmentStatus.READY_FOR_DISPATCH).count())
                .build();
    }

    public void updateShipmentStatus(ShipmentStatus shipmentStatus, UUID shipmentId){
        Shipment shipmentSavedEntity = shipmentRepository.findById(shipmentId).orElseThrow(() -> new NoResourceFoundException("Shipment not found with ID: " + shipmentId));
        shipmentSavedEntity.setShipmentStatus(shipmentStatus);
        shipmentRepository.save(shipmentSavedEntity);
        trackingRepository.save(Tracking.builder()
                .shipment(shipmentSavedEntity)
                .heading(ShipmentStatusUtil.getHeading(shipmentStatus))
                .description(ShipmentStatusUtil.getDescription(shipmentStatus))
                .timestamp(shipmentSavedEntity.getCreatedAt())
                .shipmentStatus(shipmentStatus)
                .build());
    }

}


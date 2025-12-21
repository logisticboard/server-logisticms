package com.example.logisticms.service.impl;


import com.example.logisticms.dto.ShipmentCreateRequest;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl {

    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;

    public Shipment createShipment(ShipmentCreateRequest shipment, Truck truck, FleetOperator fleetOperator) {
        ShipmentStatus shipmentStatus = ShipmentStatus.CREATED;
        if(truck != null && truck.getAssignedDriver() != null && !truck.getAssignedDriver().isEmpty())
            shipmentStatus = ShipmentStatus.READY_FOR_DISPATCH;
        else if(truck != null)
            shipmentStatus = ShipmentStatus.TRUCK_ASSIGNED;
        Shipment shipmentSavedEntity = shipmentRepository.save(ShipmentMapper.toEntity(shipment, truck, shipmentStatus, fleetOperator));
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


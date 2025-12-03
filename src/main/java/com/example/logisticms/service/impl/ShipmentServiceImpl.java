package com.example.logisticms.service.impl;


import com.example.logisticms.dto.ShipmentCreateRequest;
import com.example.logisticms.dto.ShipmentSummaryResponse;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl {

    private final ShipmentRepository shipmentRepository;

//    @Autowired
//    private TruckRepository truckRepository;

//    @Autowired
//    private DriverRepository driverRepository;

    public Shipment createShipment(ShipmentCreateRequest shipment, Truck truck, FleetOperator fleetOperator) {
        ShipmentStatus shipmentStatus = ShipmentStatus.CREATED;
        if(truck != null && truck.getAssignedDriver() != null && !truck.getAssignedDriver().isEmpty())
            shipmentStatus = ShipmentStatus.READY_FOR_DISPATCH;
        else if(truck != null)
            shipmentStatus = ShipmentStatus.TRUCK_ASSIGNED;

        return shipmentRepository.save(ShipmentMapper.toEntity(shipment, truck, shipmentStatus, fleetOperator));
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

//    public List<Shipment> getAllShipments() {
//        return shipmentRepository.findAll();
//    }

//    public Shipment getShipmentById(Long id) {
//        return shipmentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + id));
//    }

//    public Shipment updateStatus(Long id, String status) {
//        Shipment shipment = getShipmentById(id);
//        shipment.setStatus(ShipmentStatus.valueOf(status.toUpperCase()));
//
//        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
//            shipment.setActualDeliveryDate(LocalDateTime.now());
//        }
//
//        return shipmentRepository.save(shipment);
//    }

//    public Shipment assignTruckAndDriver(Long shipmentId, Long truckId, Long driverId) {
//        Shipment shipment = getShipmentById(shipmentId);
//        Truck truck = truckRepository.findById(truckId)
//                .orElseThrow(() -> new RuntimeException("Truck not found"));
//        Driver driver = driverRepository.findById(driverId)
//                .orElseThrow(() -> new RuntimeException("Driver not found"));
//
//        shipment.setTruck(truck);
//        shipment.setDriver(driver);
//
//        truck.setStatus(TruckStatus.ON_TRIP);
//        driver.setStatus(DriverStatus.ON_TRIP);
//
//        truckRepository.save(truck);
//        driverRepository.save(driver);
//
//        return shipmentRepository.save(shipment);
//    }

//    public void deleteShipment(Long id) {
//        shipmentRepository.deleteById(id);
//    }
}


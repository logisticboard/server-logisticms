package com.example.logisticms.service.impl;


import com.example.logisticms.entity.*;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShipmentServiceImpl {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private DriverRepository driverRepository;

    public Shipment createShipment(Shipment shipment) {
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setExpectedDeliveryDate(LocalDateTime.now().plusDays(2)); // default ETA
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found with ID: " + id));
    }

    public Shipment updateStatus(Long id, String status) {
        Shipment shipment = getShipmentById(id);
        shipment.setStatus(ShipmentStatus.valueOf(status.toUpperCase()));

        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
            shipment.setActualDeliveryDate(LocalDateTime.now());
        }

        return shipmentRepository.save(shipment);
    }

    public Shipment assignTruckAndDriver(Long shipmentId, Long truckId, Long driverId) {
        Shipment shipment = getShipmentById(shipmentId);
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new RuntimeException("Truck not found"));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        shipment.setTruck(truck);
        shipment.setDriver(driver);

        truck.setStatus(TruckStatus.ON_TRIP);
        driver.setStatus(DriverStatus.ON_TRIP);

        truckRepository.save(truck);
        driverRepository.save(driver);

        return shipmentRepository.save(shipment);
    }

    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }
}


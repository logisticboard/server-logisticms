package com.example.logisticms.service.impl;


import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.LocationUpdate;
//import com.example.logisticms.entity.Shipment;
//import com.example.logisticms.repository.DriverRepository;
//import com.example.logisticms.repository.LocationUpdateRepository;
//import com.example.logisticms.repository.ShipmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackingServiceImpl {

//    @Autowired
//    private LocationUpdateRepository locationUpdateRepository;
//
//    @Autowired
//    private DriverRepository driverRepository;
//
//    @Autowired
//    private ShipmentRepository shipmentRepository;

//    public LocationUpdate addLocationUpdate(Long driverId, Long shipmentId, Double lat, Double lon) {
//        Driver driver = driverRepository.findById(driverId)
//                .orElseThrow(() -> new RuntimeException("Driver not found"));
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> new RuntimeException("Shipment not found"));
//
//        LocationUpdate update = LocationUpdate.builder()
//                .driver(driver)
//                .shipment(shipment)
//                .latitude(lat)
//                .longitude(lon)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        driver.setCurrentLat(lat);
//        driver.setCurrentLon(lon);
//        driverRepository.save(driver);
//
//        return locationUpdateRepository.save(update);
//    }
//
//    public List<LocationUpdate> getShipmentTracking(Long shipmentId) {
//        return locationUpdateRepository.findByShipmentIdOrderByTimestampDesc(shipmentId);
//    }
}


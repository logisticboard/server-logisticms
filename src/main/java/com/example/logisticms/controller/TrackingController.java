package com.example.logisticms.controller;


import com.example.logisticms.entity.LocationUpdate;
import com.example.logisticms.service.impl.TrackingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

//    @Autowired
//    private TrackingServiceImpl trackingService;

//    @PostMapping("/driver/{driverId}/shipment/{shipmentId}")
//    public LocationUpdate addLocationUpdate(@PathVariable Long driverId,
//                                            @PathVariable Long shipmentId,
//                                            @RequestParam Double lat,
//                                            @RequestParam Double lon) {
//        return trackingService.addLocationUpdate(driverId, shipmentId, lat, lon);
//    }

//    @GetMapping("/shipment/{shipmentId}")
//    public List<LocationUpdate> getShipmentTracking(@PathVariable Long shipmentId) {
//        return trackingService.getShipmentTracking(shipmentId);
//    }
}


package com.example.logisticms.controller;


import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverShipment;
import com.example.logisticms.dto.TrackingDto;
import com.example.logisticms.entity.LocationUpdate;
import com.example.logisticms.service.impl.TrackingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingServiceImpl trackingService;

//    @PostMapping("/driver/{driverId}/shipment/{shipmentId}")
//    public LocationUpdate addLocationUpdate(@PathVariable Long driverId,
//                                            @PathVariable Long shipmentId,
//                                            @RequestParam Double lat,
//                                            @RequestParam Double lon) {
//        return trackingService.addLocationUpdate(driverId, shipmentId, lat, lon);
//    }

}


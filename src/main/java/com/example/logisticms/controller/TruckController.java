package com.example.logisticms.controller;



import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.service.impl.TruckServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
public class TruckController {

    private final TruckServiceImpl truckService;

//    @PostMapping
//    public ApiResponseDTO<Truck> createOrUpdateTruck(@RequestBody Truck truck) {
//        return ApiResponseDTO.<DriverDto>builder()
//                .message("Driver created successfully")
//                .success(true)
//                .data(truckService.createOrUpdateTruck(truck))
//                .build();
//    }

//    @GetMapping
//    public List<Truck> getAllTrucks() {
//        return truckService.getAllTrucks();
//    }
//
//    @GetMapping("/{id}")
//    public Truck getTruckById(@PathVariable Long id) {
//        return truckService.getTruckById(id);
//    }
//
//    @PutMapping("/{id}")
//    public Truck updateTruck(@PathVariable Long id, @RequestBody Truck updatedTruck) {
//        return truckService.updateTruck(id, updatedTruck);
//    }
//
//    @PutMapping("/{id}/status")
//    public Truck updateTruckStatus(@PathVariable Long id, @RequestParam String status) {
//        return truckService.updateStatus(id, status);
//    }

//    @PutMapping("/{truckId}/assign-driver/{driverId}")
//    public Truck assignDriverToTruck(@PathVariable Long truckId, @PathVariable Long driverId) {
//        return truckService.assignDriver(truckId, driverId);
//    }
}


package com.example.logisticms.controller;



import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.dto.TruckDto;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.service.impl.TruckServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trucks")
@RequiredArgsConstructor
public class TruckController {





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


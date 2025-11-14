package com.example.logisticms.controller;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.service.impl.DriverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverServiceImpl driverService;

    public DriverController(DriverServiceImpl driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ApiResponseDTO<Driver> createDriver(@RequestBody DriverDto driver) {
        return ApiResponseDTO.<Driver>builder()
                .message("Driver created successfully")
                .success(true)
                .data(driverService.createDriver(driver))
                .build();
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    public Driver updateDriver(@PathVariable Long id, @RequestBody Driver updatedDriver) {
        return driverService.updateDriver(id, updatedDriver);
    }

    @PutMapping("/{id}/status")
    public Driver updateDriverStatus(@PathVariable Long id, @RequestParam String status) {
        return driverService.updateStatus(id, status);
    }

    @PutMapping("/{id}/location")
    public Driver updateDriverLocation(@PathVariable Long id,
                                       @RequestParam Double lat,
                                       @RequestParam Double lon) {
        return driverService.updateLocation(id, lat, lon);
    }
}

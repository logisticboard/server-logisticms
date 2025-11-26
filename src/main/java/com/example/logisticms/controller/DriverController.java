package com.example.logisticms.controller;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.service.impl.DriverServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverService;
    private final FleetOperatorServiceImpl fleetOperatorService;

//
//    @PostMapping
//    public ApiResponseDTO<DriverDto> createOrUpdateDriverProfile(@RequestBody DriverDto driver) {
//        UUID userId =  UUID.fromString((String)SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal());
//        return ApiResponseDTO.<DriverDto>builder()
//                .message("Driver created successfully")
//                .success(true)
//                .data(driverService.createOrUpdateDriver(driver, userId))
//                .build();
//    }

    @GetMapping
    public ApiResponseDTO<List<DriverDto>> getAllDrivers() {
        UUID userId = UUID.fromString((SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).toString());
        FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorForMember(userId);
        return ApiResponseDTO.<List<DriverDto>>builder()
                .message("Drivers retrieved successfully")
                .success(true)
                .data(driverService.getAllDriverByFleetOperator(fleetOperator))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponseDTO<DriverDto> getDriverById(@PathVariable UUID id) {
//        TODO: instead of uuid id we should use email as user id is confidential
//        TODO: Add authorization to ensure drivers can only access their own profiles or fleetowner of driver can access their drivers
        return ApiResponseDTO.<DriverDto>builder()
                .message("Driver Profile retrieved successfully")
                .success(true)
                .data(driverService.getDriverById(id))
                .build();
    }
}

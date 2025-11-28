package com.example.logisticms.service.impl;


import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.DriverStatus;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DriverServiceImpl {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverDto createOrUpdateDriver(DriverDto driverDto, UUID userId) {
        Driver driver = DriverMapper.toEntity(driverDto);
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setId(userId);
        driverRepository.save(driver);
        return driverDto;
    }



    public DriverDto getDriverById(UUID id) {
        return DriverMapper.toDto(
                driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + id))
        );
    }



    public List<DriverDto> getAllDriverByFleetOperator(FleetOperator fleetOperator) {
        List<Driver> drivers = driverRepository.findByFleetOperator(fleetOperator);
        Map<String, List<String>> truckToDriverMap = drivers.stream()
                .filter(driver -> driver.getTruck() != null)
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                driver -> driver.getTruck().getRegistrationNumber(),
                                java.util.stream.Collectors.mapping(
                                        Driver::getName,
                                        java.util.stream.Collectors.toList()
                                )
                        )
                );
        System.out.println(truckToDriverMap);
        return drivers.stream()
                .map(DriverMapper::toDto)
                .toList();

    }

//    public Driver updateDriver(Long id, Driver updatedDriver) {
//        Driver existing = getDriverById(id);
//        existing.setName(updatedDriver.getName());
//        existing.setPhoneNumber(updatedDriver.getPhoneNumber());
//        existing.setLicenseNumber(updatedDriver.getLicenseNumber());
//        return driverRepository.save(existing);
//    }
//
//    public Driver updateStatus(Long id, String status) {
//        Driver driver = getDriverById(id);
//        driver.setStatus(DriverStatus.valueOf(status.toUpperCase()));
//        return driverRepository.save(driver);
//    }
//
//    public Driver updateLocation(Long id, Double lat, Double lon) {
//        Driver driver = getDriverById(id);
//        driver.setCurrentLat(lat);
//        driver.setCurrentLon(lon);
//        return driverRepository.save(driver);
//    }


}

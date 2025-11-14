package com.example.logisticms.service.impl;


import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.DriverStatus;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(DriverDto driverDto) {
        Driver driver = DriverMapper.toEntity(driverDto);
        driver.setStatus(DriverStatus.AVAILABLE);
        return driverRepository.save(DriverMapper.toEntity(driverDto));
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + id));
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver existing = getDriverById(id);
        existing.setName(updatedDriver.getName());
        existing.setPhoneNumber(updatedDriver.getPhoneNumber());
        existing.setLicenseNumber(updatedDriver.getLicenseNumber());
        return driverRepository.save(existing);
    }

    public Driver updateStatus(Long id, String status) {
        Driver driver = getDriverById(id);
        driver.setStatus(DriverStatus.valueOf(status.toUpperCase()));
        return driverRepository.save(driver);
    }

    public Driver updateLocation(Long id, Double lat, Double lon) {
        Driver driver = getDriverById(id);
        driver.setCurrentLat(lat);
        driver.setCurrentLon(lon);
        return driverRepository.save(driver);
    }
}

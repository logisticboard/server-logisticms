package com.example.logisticms.service.impl;


import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.entity.TruckStatus;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckServiceImpl {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private DriverRepository driverRepository;

    public Truck createTruck(Truck truck) {
        truck.setStatus(TruckStatus.AVAILABLE);
        return truckRepository.save(truck);
    }

    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    public Truck getTruckById(Long id) {
        return truckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Truck not found with ID: " + id));
    }

    public Truck updateTruck(Long id, Truck updatedTruck) {
        Truck existing = getTruckById(id);
        existing.setModel(updatedTruck.getModel());
        existing.setCapacity(updatedTruck.getCapacity());
        existing.setRegistrationNumber(updatedTruck.getRegistrationNumber());
        return truckRepository.save(existing);
    }

    public Truck updateStatus(Long id, String status) {
        Truck truck = getTruckById(id);
        truck.setStatus(TruckStatus.valueOf(status.toUpperCase()));
        return truckRepository.save(truck);
    }

    public Truck assignDriver(Long truckId, Long driverId) {
        Truck truck = getTruckById(truckId);
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));

        truck.setAssignedDriver(driver);
        driver.setTruck(truck);

        driverRepository.save(driver);
        return truckRepository.save(truck);
    }
}


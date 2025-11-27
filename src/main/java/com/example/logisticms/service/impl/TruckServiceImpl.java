package com.example.logisticms.service.impl;


import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.dto.TruckDto;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.entity.TruckStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.TruckMapper;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl {

    private final TruckRepository truckRepository;
    private final FleetOperatorServiceImpl fleetOperatorService;
//    private final DriverRepository driverRepository;

//    public TruckDto createOrUpdateTruck(Truck truck) {
//        truck.setStatus(TruckStatus.AVAILABLE);
//        return truckRepository.save(truck);
//    }

//    public List<Truck> getAllTrucks() {
//        return truckRepository.findAll();
//    }
//
//    public Truck getTruckById(Long id) {
//        return truckRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Truck not found with ID: " + id));
//    }

//    public Truck updateTruck(Long id, Truck updatedTruck) {
//        Truck existing = getTruckById(id);
//        existing.setModel(updatedTruck.getModel());
//        existing.setCapacity(updatedTruck.getCapacity());
//        existing.setRegistrationNumber(updatedTruck.getRegistrationNumber());
//        return truckRepository.save(existing);
//    }

//    public Truck updateStatus(Long id, String status) {
//        Truck truck = getTruckById(id);
//        truck.setStatus(TruckStatus.valueOf(status.toUpperCase()));
//        return truckRepository.save(truck);
//    }

    public TruckDto createTruck(TruckDto truckDto, UUID fleetOperatorId) {
        FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);
        return TruckMapper.toDto(truckRepository.save(Truck.builder()
                .registrationNumber(truckDto.getRegistrationNumber())
                .model(truckDto.getModel())
                .capacity(truckDto.getCapacity())
                .description(truckDto.getDescription())
                .status(TruckStatus.AVAILABLE)
                .fleetOperator(fleetOperator)
                .build()));
    }

    public TruckDto updateTruck(UUID truckId, TruckDto truckDto, UUID fleetOperatorId) {
        FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);
        Truck existingTruck = truckRepository.findById(truckId)
                .orElseThrow(() -> new NoResourceFoundException("Truck not found with ID: " + truckId));
        existingTruck.setRegistrationNumber(truckDto.getRegistrationNumber());
        existingTruck.setModel(truckDto.getModel());
        existingTruck.setCapacity(truckDto.getCapacity());
        existingTruck.setDescription(truckDto.getDescription());
        existingTruck.setStatus(truckDto.getTruckStatus());
        existingTruck.setFleetOperator(fleetOperator);
        return TruckMapper.toDto(truckRepository.save(existingTruck));
    }


//    public Truck assignDriver(Long truckId, Long driverId) {
//        Truck truck = getTruckById(truckId);
//        Driver driver = driverRepository.findById(driverId)
//                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
//
////        truck.setAssignedDriver(driver);
//        driver.setTruck(truck);
//
//        driverRepository.save(driver);
//        return truckRepository.save(truck);
//    }
}


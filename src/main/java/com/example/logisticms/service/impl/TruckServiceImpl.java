package com.example.logisticms.service.impl;


import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.dto.TruckDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.entity.TruckStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.DriverMapper;
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
    private final DriverRepository driverRepository;
//    private final DriverRepository driverRepository;

//    public TruckDto createOrUpdateTruck(Truck truck) {
//        truck.setStatus(TruckStatus.AVAILABLE);
//        return truckRepository.save(truck);
//    }

//    public List<Truck> getAllTrucks() {
//        return truckRepository.findAll();
//    }
//
    public Truck getTruckById(UUID id) {
        return truckRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Truck not found with ID: " + id));
    }


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

        List<Driver> drivers = driverRepository.findAllById(
                truckDto.getDrivers().stream().map(DriverDto::getDriverId).toList()
        );

        if (drivers.size() != truckDto.getDrivers().size()) {
            throw new NoResourceFoundException("One or more drivers not found for the given IDs");
        }

        Truck truck = Truck.builder()
                .registrationNumber(truckDto.getRegistrationNumber())
                .model(truckDto.getModel())
                .capacity(truckDto.getCapacity())
                .description(truckDto.getDescription())
                .status(TruckStatus.AVAILABLE)
                .fleetOperator(fleetOperator)
                .build();

        truck = truckRepository.save(truck); // save & get ID

        for (Driver driver : drivers) {
            driver.setTruck(truck);
        }

        driverRepository.saveAll(drivers);

        truck.setAssignedDriver(drivers);

        TruckDto responseTruckDto = TruckMapper.toDto(truck);
        Truck finalTruck = truck;
        responseTruckDto.setDrivers(
                drivers.stream().map(driver -> DriverDto.builder()
                        .driverId(driver.getId())
                        .name(driver.getName())
                        .licenseNumber(driver.getLicenseNumber())
                        .phoneNumber(driver.getPhoneNumber())
                        .driverStatus(driver.getStatus())
                        .assignedTruck(TruckDto
                                .builder()
                                .truckId(finalTruck.getId())
                                .model(finalTruck.getModel())
                                .build())
                        .build()).toList()
        );

        return responseTruckDto;
    }

    public TruckDto updateTruck(UUID truckId, TruckDto truckDto, UUID fleetOperatorId) {
        FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);
        Truck existingTruck = truckRepository.findById(truckId)
                .orElseThrow(() -> new NoResourceFoundException("Truck not found with ID: " + truckId));
        List<Driver> drivers = driverRepository.findAllById(
                truckDto.getDrivers().stream().map(DriverDto::getDriverId).toList()
        );
        if(drivers.size() != truckDto.getDrivers().size()) {
            throw new NoResourceFoundException("One or more drivers not found for the given IDs");
        }
        if (existingTruck.getAssignedDriver() != null) {
            for (Driver d : existingTruck.getAssignedDriver()) {
                d.setTruck(null);
            }
        }
        existingTruck.setRegistrationNumber(truckDto.getRegistrationNumber());
        existingTruck.setModel(truckDto.getModel());
        existingTruck.setCapacity(truckDto.getCapacity());
        existingTruck.setDescription(truckDto.getDescription());
        existingTruck.setStatus(truckDto.getTruckStatus());
        existingTruck.setFleetOperator(fleetOperator);
        existingTruck.setAssignedDriver(drivers);

        for(Driver driver : drivers) {
            driver.setTruck(existingTruck);
        }
        TruckDto responseTruckDto = TruckMapper.toDto(truckRepository.save(existingTruck));
        responseTruckDto.setDrivers(
                drivers.stream().map(driver -> DriverDto.builder()
                        .driverId(driver.getId())
                        .name(driver.getName())
                        .licenseNumber(driver.getLicenseNumber())
                        .phoneNumber(driver.getPhoneNumber())
                        .driverStatus(driver.getStatus())
                        .assignedTruck(
                                driver.getTruck() != null ? TruckMapper.toDto(driver.getTruck()) : null
                        )
                        .build()).toList()
        );
        return responseTruckDto;
    }

    public Truck getTruckByFleetOperatorIdAndTruckId(UUID fleetOperatorId, UUID truckId) {
        return truckRepository.findByIdAndFleetOperatorId(truckId, fleetOperatorId)
                .orElseThrow(() -> new NoResourceFoundException("Truck not found with ID: " + truckId +
                        " for Fleet Operator ID: " + fleetOperatorId));
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


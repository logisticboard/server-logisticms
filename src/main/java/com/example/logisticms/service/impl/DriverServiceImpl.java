package com.example.logisticms.service.impl;


import com.example.logisticms.dto.*;
import com.example.logisticms.entity.*;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.mapper.TrackingMapper;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TrackingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class DriverServiceImpl {

    private final DriverRepository driverRepository;
    private final ShipmentRepository shipmentRepository;
    private final TrackingRepository trackingRepository;


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

    public List<DriverShipment> getAllShipmentsForDriver(String phoneNumber) {
        List<DriverShipment> shipments =  new ArrayList<>();
        for(Driver driver : driverRepository.findByPhoneNumber(phoneNumber)) {
            if(driver.getTruck() != null) {
                 shipments.addAll(driver.getTruck().getShipments()
                        .stream()
                        .map(shipment -> ShipmentMapper.toDriverShipmentDto(shipment, driver.getFleetOperator().getName()))
                        .toList());
            }
        }
        return shipments;
    }

    public List<TrackingDto> getShipmentTrack(UUID shipmentUuid, String phoneNumber) {
        return trackingRepository.findByShipment_IdOrderByTimestampAsc(shipmentUuid)
                .stream()
                .map(TrackingMapper::trackingDto)
                .toList();
    }
}

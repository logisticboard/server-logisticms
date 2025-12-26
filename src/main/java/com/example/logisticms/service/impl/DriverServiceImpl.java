package com.example.logisticms.service.impl;


import com.example.logisticms.dto.*;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.mapper.TrackingMapper;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.ShipmentRepository;
import com.example.logisticms.repository.TrackingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<DriverShipment> getAllShipmentsForDriver(
            String phoneNumber,
            ShipmentStatus shipmentStatus,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("pickupDate").descending());
        List<DriverShipment> result = new ArrayList<>();

        for (Driver driver : driverRepository.findByPhoneNumber(phoneNumber)) {
            if (driver.getTruck() != null) {
                Page<Shipment> shipmentPage =
                        shipmentRepository.findByTruckAndStatus(
                                driver.getTruck().getId(),
                                shipmentStatus,
                                pageable
                        );

                result.addAll(
                        shipmentPage.getContent()
                                .stream()
                                .map(s -> ShipmentMapper.toDriverShipmentDto(
                                        s,
                                        driver.getFleetOperator().getName()
                                ))
                                .toList()
                );
            }
        }
        return result;
    }


    public List<TrackingDto> getShipmentTrack(UUID shipmentUuid, String phoneNumber) {
        return trackingRepository.findByShipment_IdOrderByTimestampAsc(shipmentUuid)
                .stream()
                .map(TrackingMapper::trackingDto)
                .toList();
    }

    public List<DriverFleetOperatorResponse> getAllFleetOperatorsNameForDriver(String phoneNumber) {
        return driverRepository.findByPhoneNumber(phoneNumber)
                .stream()
                .map(driver -> DriverFleetOperatorResponse.builder()
                        .fleetOperatorName(driver.getFleetOperator().getName())
                        .fleetOperatorUid(driver.getFleetOperator().getId())
                        .build())
                .distinct()
                .toList();
    }

    public DriverProfileForFleetOperator getDriverProfileForFleetOperator(String phoneNumber, UUID fleetOperatorId) {
        return driverRepository.findByPhoneNumberAndFleetOperator_Id(phoneNumber, fleetOperatorId)
                .stream()
                .findFirst()
                .map(driver -> DriverProfileForFleetOperator.builder()
                        .fullName(driver.getName())
                        .licenseNumber(driver.getLicenseNumber())
                        .build())
                .orElseThrow(() -> new NoResourceFoundException("Driver not found for the given fleet operator"));
    }


}

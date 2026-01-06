package com.example.logisticms.service.impl;


import com.example.logisticms.dto.*;
import com.example.logisticms.entity.*;
import com.example.logisticms.entity.enums.ShipmentAssignment;
import com.example.logisticms.entity.enums.ShipmentStatus;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.mapper.ShipmentMapper;
import com.example.logisticms.mapper.TrackingMapper;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.ShipmentAssignmentRepository;
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
    private final ShipmentAssignmentRepository shipmentAssignmentRepository;


    public DriverDto createOrUpdateDriver(DriverDto driverDto, UUID userId) {
        Driver driver = DriverMapper.toEntity(driverDto);
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setId(userId);
        driverRepository.save(driver);
        return driverDto;
    }


    public DriverDto getDriverById(UUID id) {
        return DriverMapper.toDto(driverRepository.findById(id).orElseThrow(() -> new RuntimeException("Driver not found with ID: " + id)));
    }


    public List<DriverDto> getAllDriverByFleetOperator(FleetOperator fleetOperator) {
        List<Driver> drivers = driverRepository.findByFleetOperator(fleetOperator);
        return drivers.stream().map(DriverMapper::toDto).toList();

    }

    public List<DriverShipment> getAllShipmentsForDriver(String phoneNumber, ShipmentStatus shipmentStatus, int page, int size) {
        if(phoneNumber.startsWith("+91"))
            phoneNumber = phoneNumber.substring(3);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "shipment.createdAt"));
        Page<ShipmentAssignment> assignments ;
        if(shipmentStatus != null)
            assignments = shipmentAssignmentRepository.findByDriverPhoneAndShipmentStatus(phoneNumber, shipmentStatus, pageable);
        else assignments = shipmentAssignmentRepository.findShipmentsByDriverPhone(phoneNumber, pageable);
        return assignments.stream()
                .map(assignment -> ShipmentMapper.toDriverShipmentDto(assignment.getShipment()))
                .toList();
    }


    public List<TrackingDto> getShipmentTrack(UUID shipmentUuid, String phoneNumber) {
        return trackingRepository.findByShipment_IdOrderByTimestampAsc(shipmentUuid).stream().map(TrackingMapper::trackingDto).toList();
    }

    public List<DriverFleetOperatorResponse> getAllFleetOperatorsNameForDriver(String phoneNumber) {
        if(phoneNumber.startsWith("+91"))
            phoneNumber = phoneNumber.substring(3);
        return driverRepository.findByPhoneNumber(phoneNumber).stream().map(driver -> DriverFleetOperatorResponse.builder().fleetOperatorName(driver.getFleetOperator().getName()).fleetOperatorUid(driver.getFleetOperator().getId()).build()).distinct().toList();
    }

    public DriverProfileForFleetOperator getDriverProfileForFleetOperator(String phoneNumber, UUID fleetOperatorId) {
        if(phoneNumber.startsWith("+91"))
            phoneNumber = phoneNumber.substring(3);
        return driverRepository.findByPhoneNumberAndFleetOperator_Id(phoneNumber, fleetOperatorId).stream().findFirst().map(driver -> DriverProfileForFleetOperator.builder().fullName(driver.getName()).licenseNumber(driver.getLicenseNumber()).build()).orElseThrow(() -> new NoResourceFoundException("Driver not found for the given fleet operator"));
    }


}

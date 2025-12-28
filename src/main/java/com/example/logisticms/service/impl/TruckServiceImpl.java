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




    public TruckDto createTruck(TruckDto truckDto, UUID fleetOperatorId) {
        FleetOperator fleetOperator = fleetOperatorService.getFleetOperatorById(fleetOperatorId);

        Truck truck = Truck.builder()
                .registrationNumber(truckDto.getRegistrationNumber())
                .model(truckDto.getModel())
                .capacity(truckDto.getCapacity())
                .description(truckDto.getDescription())
                .status(TruckStatus.AVAILABLE)
                .fleetOperator(fleetOperator)
                .build();

        truck = truckRepository.save(truck);
        return TruckMapper.toDto(truck);
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

    public Truck getTruckByFleetOperatorIdAndTruckId(UUID fleetOperatorId, UUID truckId) {
        return truckRepository.findByIdAndFleetOperatorId(truckId, fleetOperatorId)
                .orElseThrow(() -> new NoResourceFoundException("Truck not found with ID: " + truckId +
                        " for Fleet Operator ID: " + fleetOperatorId));
    }

}


package com.example.logisticms.service.impl;

import com.example.logisticms.dto.DriverDto;
import com.example.logisticms.dto.FleetOperatorDto;
import com.example.logisticms.dto.FleetOperatorRoleCreate;
import com.example.logisticms.dto.TruckDto;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.DriverStatus;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.entity.Truck;
import com.example.logisticms.exception.NoResourceFoundException;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.mapper.DriverMapper;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.mapper.TruckMapper;
import com.example.logisticms.repository.DriverRepository;
import com.example.logisticms.repository.FleetOperatorRepository;
import com.example.logisticms.repository.FleetOperatorMemberRepository;
import com.example.logisticms.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FleetOperatorServiceImpl {
    private final FleetOperatorMemberRepository fleetOperatorMemberRepository;
    private final FleetOperatorRepository fleetOperatorRepository;
    private final DriverRepository driverRepository;
    private final TruckRepository truckRepository;
    public FleetOperator getFleetOperatorForMember(UUID userId) {
        return fleetOperatorMemberRepository.findById(userId)
                .orElseThrow(()-> new NoResourceFoundException("Fleet Operator not found for given user"))
                .getFleetOperator();
    }
    public FleetOperator getFleetOperatorById(UUID fleetOperatorId) {
        return fleetOperatorRepository.findById(fleetOperatorId)
                .orElseThrow(()-> new NoResourceFoundException("Fleet Operator not found for given id"));
    }

    public void createFleetOperatorRoles(List<FleetOperatorRoleCreate> fleetOperatorRoleCreateList) {
//        TODO: call loginms for user id corresponding to email
//        TODO: send email notification to user asking to confirm for assigned with new roles and ask in frontend side to confirm
//        fleetOperatorRoleRepository.saveAll(
//                fleetOperatorRoleCreateList.stream()
//                        .map(roleCreate-> FleetOperatorRoleMapper.toEntity(roleCreate.userId(), roleCreate.role()))
//                        .toList()
//        );
    }

    public FleetOperator createFleetOperator(FleetOperatorDto fleetOperatorDto) {
        return fleetOperatorRepository.save(
                FleetOperatorMapper.toEntity(
                        fleetOperatorDto
                )
        );
    }

    public FleetOperator updateFleetOperator(FleetOperatorDto fleetOperatorDto, UUID companyId) {
        FleetOperator fleetOperator = FleetOperatorMapper.toEntity(
                fleetOperatorDto
        );
        fleetOperator.setId(companyId);
        return fleetOperatorRepository.save(fleetOperator);

    }

    public List<TruckDto> getTrucksByFleetOperator(UUID fleetOperatorId) {
        return fleetOperatorRepository.findById(fleetOperatorId)
                .orElseThrow(()-> new NoResourceFoundException("Fleet Operator not found for given id"))
                .getTrucks()
                .stream()
                .map(truck-> {
                    List<DriverDto> assignedDrivers = new ArrayList<>();
                    return TruckDto.builder()
                            .truckId(truck.getId())
                            .registrationNumber(truck.getRegistrationNumber())
                            .model(truck.getModel())
                            .capacity(truck.getCapacity())
                            .description(truck.getDescription())
                            .truckStatus(truck.getStatus())
                            .build();
                })
                .toList();
    }


    public DriverDto createDriver(DriverDto driverDto, UUID fleetOperatorId) {
        FleetOperator fleetOperator = getFleetOperatorById(fleetOperatorId);
        if(driverRepository.existsByFleetOperatorAndPhoneNumber(fleetOperator, driverDto.getPhoneNumber())) {
            throw new UnauthorizedOperationException("Driver with given phone number already exists for this fleet operator");
        }

        Driver driver = driverRepository.save(
                Driver.builder()
                        .name(driverDto.getName())
                        .licenseNumber(driverDto.getLicenseNumber())
                        .phoneNumber(driverDto.getPhoneNumber())
                        .status(DriverStatus.AVAILABLE)
                        .fleetOperator(fleetOperator)
                        .build());
        return DriverDto.builder()
                .name(driver.getName())
                .driverId(driver.getId())
                .licenseNumber(driver.getLicenseNumber())
                .phoneNumber(driver.getPhoneNumber())
                .driverStatus(driver.getStatus())
                .build();
    }

    public List<DriverDto> getDriversByFleetOperator(UUID fleetOperatorId) {
        return fleetOperatorRepository.findById(fleetOperatorId)
                .orElseThrow(()-> new NoResourceFoundException("Fleet Operator not found for given id"))
                .getDrivers()
                .stream()
                .map(driver -> DriverDto.builder()
                        .name(driver.getName())
                        .driverId(driver.getId())
                        .licenseNumber(driver.getLicenseNumber())
                        .phoneNumber(driver.getPhoneNumber())
                        .driverStatus(driver.getStatus())
                        .build()
                )
                .toList();
    }

    public DriverDto updateDriver(UUID driverId, DriverDto driverDto, UUID fleetOperatorId) {
        Driver existingDriver = driverRepository.findById(driverId)
                .orElseThrow(()-> new NoResourceFoundException("Driver not found for given id"));
        existingDriver.setName(driverDto.getName());
        existingDriver.setLicenseNumber(driverDto.getLicenseNumber());
        existingDriver.setPhoneNumber(driverDto.getPhoneNumber());
        driverRepository.save(existingDriver);
        return DriverDto.builder()
                .name(existingDriver.getName())
                .driverId(existingDriver.getId())
                .licenseNumber(existingDriver.getLicenseNumber())
                .phoneNumber(existingDriver.getPhoneNumber())
                .driverStatus(existingDriver.getStatus())
                .build();
    }

    public List<TruckDto> getUnAssignedTrucksByFleetOperator(UUID fleetOperatorId) {
        return truckRepository.findUnassignedTrucks(fleetOperatorId)
                .stream()
                .map(TruckMapper::toDto)
                .toList();
    }

    public List<DriverDto> getUnAssignedDriverByFleetOperator(UUID fleetOperatorId) {
        return driverRepository.findUnassignedDrivers(fleetOperatorId)
                .stream()
                .map(driver -> DriverDto.builder()
                        .name(driver.getName())
                        .driverId(driver.getId())
                        .licenseNumber(driver.getLicenseNumber())
                        .phoneNumber(driver.getPhoneNumber())
                        .driverStatus(driver.getStatus())
                        .build()
                )
                .toList();
    }
}

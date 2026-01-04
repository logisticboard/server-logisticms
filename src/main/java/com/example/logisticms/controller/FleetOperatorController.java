package com.example.logisticms.controller;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import com.example.logisticms.service.impl.TruckServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fleetoperators")
@RequiredArgsConstructor
@Validated
public class FleetOperatorController {
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;
    private final TruckServiceImpl truckService;


    @PostMapping
    public ApiResponseDTO<FleetOperatorDto> createFleetOperator(@RequestBody @Valid FleetOperatorDto fleetOperatorDto){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        FleetOperator fleetOperator = fleetOperatorService.createFleetOperator(fleetOperatorDto);
        fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
        return ApiResponseDTO.<FleetOperatorDto>builder()
                .message("Fleet Operator created successfully")
                .data(FleetOperatorMapper.toDto(fleetOperator))
                .success(true)
                .build();
    }

    @PutMapping("/{fleetOperatorId}")
    public ApiResponseDTO<FleetOperatorDto> updateFleetOperator(@RequestBody @Valid FleetOperatorDto fleetOperatorDto,
                                                                @PathVariable UUID fleetOperatorId){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            FleetOperator fleetOperator = fleetOperatorService.updateFleetOperator(fleetOperatorDto, fleetOperatorId);
            fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
            return ApiResponseDTO.<FleetOperatorDto>builder()
                    .message("Company data updated successfully")
                    .data(FleetOperatorMapper.toDto(fleetOperator))
                    .success(true)
                    .build();
        }
        throw new UnauthorizedOperationException("User is not authorized to update this Fleet Operator!");
    }


    @GetMapping
    public ApiResponseDTO<List<FleetOperatorDto>> getFleetOperators(){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        return ApiResponseDTO.<List<FleetOperatorDto>>builder()
                .message("Fleet Operators retrieved successfully")
                .data(fleetOperatorRoleService.getFleetOperatorsByUserId(userId))
                .success(true)
                .build();
    }


    @GetMapping("/members/{fleetOperatorId}")
    public ApiResponseDTO<List<FleetOperatorMembersResponse>> getMembersInFleetOperator(
                                                                @PathVariable @NotBlank
                                                                @Pattern(
                                                                        regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                                                                        message = "companyId must be a valid UUID"
                                                                )
                                                                String fleetOperatorId){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        return ApiResponseDTO.<List<FleetOperatorMembersResponse>>builder()
                    .message("Company members retrieved successfully")
                    .data(fleetOperatorRoleService.getFleetOperatorsMembers(userId, UUID.fromString(fleetOperatorId)))
                    .success(true)
                    .build();
    }

    @PostMapping("/members/{fleetOperatorId}")
    public ApiResponseDTO<Void> createOrUpdateFleetMemberData(@PathVariable @NotBlank
                                                                  @Pattern(
                                                                          regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                                                                          message = "companyId must be a valid UUID"
                                                                  )
                                                                  String fleetOperatorId,
                                                              @RequestBody @Valid MemberDataUpdateRequest data) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(UUID.fromString(fleetOperatorId), userId)){
//            TODO: check if admin is conveting to manger then there should be atleast one admin left
            fleetOperatorRoleService.upsertFleetOperatorMemberData(UUID.fromString(fleetOperatorId), data);
            return ApiResponseDTO.<Void>builder()
                    .message("Fleet Operator member data updated successfully")
                    .success(true)
                    .build();
        }
        throw new UnauthorizedOperationException("User is not authorized to update this Fleet Operator!");
    }


    @PostMapping("{fleetOperatorId}/trucks")
    public ApiResponseDTO<TruckDto> createTruck(@PathVariable UUID fleetOperatorId, @RequestBody @Valid TruckDto truckDto) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to update trucks of this Fleet Operator!");
        }
//        TODO: add driver assignment through here
        return ApiResponseDTO.<TruckDto>builder()
                .message("Driver created successfully")
                .success(true)
                .data(truckService.createTruck(truckDto, fleetOperatorId))
                .build();
    }

    @PutMapping("{fleetOperatorId}/trucks/{truckId}")
    public ApiResponseDTO<TruckDto> updateTruck(@PathVariable UUID fleetOperatorId, @PathVariable UUID truckId, @RequestBody TruckDto truckDto) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to update trucks of this Fleet Operator!");
        }
//TODO: add driver assignment through here
        return ApiResponseDTO.<TruckDto>builder()
                .message("Truck updated successfully")
                .success(true)
                .data(truckService.updateTruck(truckId, truckDto, fleetOperatorId))
                .build();
    }

    @GetMapping("/{fleetOperatorId}/trucks")
    public ApiResponseDTO<List<TruckDto>> getTrucksByFleetOperator(@PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to view trucks of this Fleet Operator!");
        }
        return ApiResponseDTO.<List<TruckDto>>builder()
                .message("Trucks retrieved successfully")
                .success(true)
                .data(fleetOperatorService.getTrucksByFleetOperator(fleetOperatorId))
                .build();
    }

    @GetMapping("/{fleetOperatorId}/drivers/unassigned")
    public ApiResponseDTO<List<DriverDto>> getUnAssignedDriverByFleetOperator(@PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to view trucks of this Fleet Operator!");
        }
        return ApiResponseDTO.<List<DriverDto>>builder()
                .message("Trucks retrieved successfully")
                .success(true)
                .data(fleetOperatorService.getUnAssignedDriverByFleetOperator(fleetOperatorId))
                .build();
    }

    @PostMapping("/{fleetOperatorId}/drivers")
    public ApiResponseDTO<DriverDto> createDriver(@PathVariable UUID fleetOperatorId, @RequestBody @Valid DriverDto driverDto) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to update drivers of this Fleet Operator!");
        }
        return ApiResponseDTO.<DriverDto>builder()
                .message("Driver created successfully")
                .success(true)
                .data(fleetOperatorService.createDriver(driverDto, fleetOperatorId))
                .build();
    }

    @PutMapping("/{fleetOperatorId}/drivers/{driverId}")
    public ApiResponseDTO<DriverDto> updateDriver(@PathVariable UUID fleetOperatorId,
                                                  @PathVariable UUID driverId,
                                                  @RequestBody @Valid DriverDto driverDto) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
//        TODO: in future driver can also update his own data(only name, phone number, license number) so need to handle that case
        if(!fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to update drivers of this Fleet Operator!");
        }
        return ApiResponseDTO.<DriverDto>builder()
                .message("Driver updated successfully")
                .success(true)
                .data(fleetOperatorService.updateDriver(driverId, driverDto, fleetOperatorId))
                .build();
    }

    @GetMapping("/{fleetOperatorId}/drivers")
    public ApiResponseDTO<List<DriverDto>> getDriversByFleetOperator(@PathVariable UUID fleetOperatorId) {
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        if(!fleetOperatorRoleService.isUserMemberOfFleetOperator(fleetOperatorId, userId)){
            throw new UnauthorizedOperationException("User is not authorized to view drivers of this Fleet Operator!");
        }
        return ApiResponseDTO.<List<DriverDto>>builder()
                .message("Drivers retrieved successfully")
                .success(true)
                .data(fleetOperatorService.getDriversByFleetOperator(fleetOperatorId))
                .build();
    }

    @GetMapping("/member/profile")
    public ApiResponseDTO<FleetOperatorMemberProfileResponse> getMembersProfileDataInFleetOperator(){
        UUID userId =  UUID.fromString((String)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
            return ApiResponseDTO.<FleetOperatorMemberProfileResponse>builder()
                    .message("Company members retrieved successfully")
                    .data(fleetOperatorRoleService.getFleetOperatorMemberProfileById(userId))
                    .success(true)
                    .build();
    }

}

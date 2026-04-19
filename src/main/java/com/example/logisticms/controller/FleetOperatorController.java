package com.example.logisticms.controller;

import com.example.logisticms.dto.*;
import com.example.logisticms.entity.Driver;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.exception.UnauthorizedOperationException;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import com.example.logisticms.service.impl.TruckServiceImpl;
import com.example.logisticms.service.util.UpstreamHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/v1/fleetoperators")
@RequiredArgsConstructor
@Validated
public class FleetOperatorController {
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;
    private final TruckServiceImpl truckService;


    @PostMapping
    public ApiResponseDTO<FleetOperatorDto> createFleetOperator(HttpServletRequest httpServletRequest, @RequestBody @Valid FleetOperatorDto fleetOperatorDto){
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        FleetOperator fleetOperator = fleetOperatorService.createFleetOperator(fleetOperatorDto);
        fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
        return ApiResponseDTO.<FleetOperatorDto>builder()
                .message("Fleet Operator created successfully")
                .data(FleetOperatorMapper.toDto(fleetOperator))
                .success(true)
                .build();
    }

    @PutMapping("/{fleetOperatorId}")
    public ApiResponseDTO<FleetOperatorDto> updateFleetOperator(HttpServletRequest httpServletRequest, @RequestBody @Valid FleetOperatorDto fleetOperatorDto,
                                                                @PathVariable UUID fleetOperatorId){
        UUID userId = UpstreamHeaderUtil.getUserId(httpServletRequest);
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
            FleetOperator fleetOperator = fleetOperatorService.updateFleetOperator(fleetOperatorDto, fleetOperatorId);
            fleetOperatorRoleService.addMemberToFleetOperator(fleetOperator, FleetOperatorRolesEnum.ADMIN, userId);
            return ApiResponseDTO.<FleetOperatorDto>builder()
                    .message("Fleet data updated successfully")
                    .data(FleetOperatorMapper.toDto(fleetOperator))
                    .success(true)
                    .build();
        }
        throw new UnauthorizedOperationException("User is not authorized to update this Fleet Operator!");
    }


    @GetMapping
    public ApiResponseDTO<List<FleetOperatorDto>> getFleetOperators(HttpServletRequest httpServletRequest){
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
        return ApiResponseDTO.<List<FleetOperatorDto>>builder()
                .message("Fleet Operators retrieved successfully")
                .data(fleetOperatorRoleService.getFleetOperatorsByUserId(userId))
                .success(true)
                .build();
    }


    @GetMapping("/members/{fleetOperatorId}")
    public ApiResponseDTO<List<FleetOperatorMembersResponse>> getMembersInFleetOperator(
            HttpServletRequest httpServletRequest, UUID fleetOperatorId){
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
        return ApiResponseDTO.<List<FleetOperatorMembersResponse>>builder()
                    .message("Company members retrieved successfully")
                    .data(fleetOperatorRoleService.getFleetOperatorsMembers(userId, fleetOperatorId, httpServletRequest))
                    .success(true)
                    .build();
    }

    @PostMapping("/members/{fleetOperatorId}")
    public ApiResponseDTO<Void> createOrUpdateFleetMemberData(
            HttpServletRequest httpServletRequest, UUID fleetOperatorId,
            @RequestBody @Valid MemberDataUpdateRequest data
    ) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
        if(fleetOperatorRoleService.isUserAdminOfFleetOperator(fleetOperatorId, userId)){
//            TODO: check if admin is conveting to manger then there should be atleast one admin left
            fleetOperatorRoleService.upsertFleetOperatorMemberData(fleetOperatorId, data, httpServletRequest);
            return ApiResponseDTO.<Void>builder()
                    .message("Fleet Operator member data updated successfully")
                    .success(true)
                    .build();
        }
        throw new UnauthorizedOperationException("User is not authorized to update this Fleet Operator!");
    }


    @PostMapping("{fleetOperatorId}/trucks")
    public ApiResponseDTO<TruckDto> createTruck(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId, @RequestBody @Valid TruckDto truckDto) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<TruckDto> updateTruck(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId, @PathVariable UUID truckId, @RequestBody TruckDto truckDto) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<List<TruckDto>> getTrucksByFleetOperator(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<List<DriverDto>> getUnAssignedDriverByFleetOperator(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<DriverDto> createDriver(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId, @RequestBody @Valid DriverDto driverDto) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<DriverDto> updateDriver(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId,
                                                  @PathVariable UUID driverId,
                                                  @RequestBody @Valid DriverDto driverDto) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<List<DriverDto>> getDriversByFleetOperator(HttpServletRequest httpServletRequest, @PathVariable UUID fleetOperatorId) {
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
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
    public ApiResponseDTO<FleetOperatorMemberProfileResponse> getMembersProfileDataInFleetOperator(HttpServletRequest httpServletRequest){
        UUID userId =  UpstreamHeaderUtil.getUserId(httpServletRequest);
            return ApiResponseDTO.<FleetOperatorMemberProfileResponse>builder()
                    .message("Company members retrieved successfully")
                    .data(fleetOperatorRoleService.getFleetOperatorMemberProfileById(userId, httpServletRequest))
                    .success(true)
                    .build();
    }

}

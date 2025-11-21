package com.example.logisticms.controller;

import com.example.logisticms.dto.ApiResponseDTO;
import com.example.logisticms.dto.FleetOperatorDto;
import com.example.logisticms.dto.FleetOperatorRolesEnum;
import com.example.logisticms.entity.FleetOperator;
import com.example.logisticms.mapper.FleetOperatorMapper;
import com.example.logisticms.service.impl.FleetOperatorRoleServiceImpl;
import com.example.logisticms.service.impl.FleetOperatorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/fleetoperators")
@RequiredArgsConstructor
public class FleetOperatorController {
    private final FleetOperatorServiceImpl fleetOperatorService;
    private final FleetOperatorRoleServiceImpl fleetOperatorRoleService;

//    @PostMapping
//    public ApiResponseDTO<Void> createFleetOperatorRole(@RequestBody List<FleetOperatorRoleCreate> fleetOperatorRoleCreateList){
//        fleetOperatorService.createFleetOperatorRoles(fleetOperatorRoleCreateList);
//        return ApiResponseDTO.<Void>builder()
//                .message("Fleet Operator Roles created successfully")
//                .success(true)
//                .build();
//    }

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
}
